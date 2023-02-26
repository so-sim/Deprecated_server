package com.sosim.server.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sosim.server.jwt.JwtFactory;
import com.sosim.server.jwt.JwtService;
import com.sosim.server.oauth.dto.OAuth2JwtResponseDto;
import com.sosim.server.oauth.dto.OAuth2TokenResponseDto;
import com.sosim.server.oauth.dto.OAuth2UserInfoDto;
import com.sosim.server.user.User;
import com.sosim.server.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OAuth2Service {
    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    private final InMemoryClientRegistrationRepository inMemoryRepository;
    private final UserRepository userRepository;
    private final JwtFactory jwtFactory;
    private final JwtService jwtService;

    @Transactional
    public OAuth2JwtResponseDto login(SocialType socialType, String authorizationCode) throws JsonProcessingException {
        // SocialType(kakao,google,naver) 에 따라 다른 inMemory 사용
        ClientRegistration type = inMemoryRepository.findByRegistrationId(socialType.name().toLowerCase());

        // OAuth2.0 Authorization Server -> Access, Refresh Token 발급
        OAuth2TokenResponseDto oAuth2Token = getToken(type, authorizationCode);

        // Access Token 으로 User 정보 획득
        User user = getUserProfile(socialType, oAuth2Token, type);

        // Sever 자체 JWT 생성 및 Refresh Token 저장
        OAuth2JwtResponseDto oAuth2JwtResponseDto = OAuth2JwtResponseDto.createOAuth2JwtResponseDto(user,
                jwtFactory.createAccessToken(String.valueOf(user.getId()), user.getEmail()),
                jwtFactory.createRefreshToken());
        jwtService.saveRefreshToken(oAuth2JwtResponseDto.getRefreshToken());

        return oAuth2JwtResponseDto;
    }

    private OAuth2TokenResponseDto getToken(ClientRegistration type, String authorizationCode) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<?> request =
                new HttpEntity<>(tokenRequest(authorizationCode, type), headers);

        RestTemplate restTemplate = new RestTemplate();

        String responseBody = restTemplate.exchange(
                type.getProviderDetails().getTokenUri(),
                HttpMethod.POST,
                request,
                String.class
        ).getBody();

        return OBJECT_MAPPER.readValue(responseBody, OAuth2TokenResponseDto.class);
    }

    private MultiValueMap<String, String> tokenRequest(String authorizationCode, ClientRegistration type) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", authorizationCode);
        formData.add("grant_type", "authorization_code");
        formData.add("redirection_uri", type.getRedirectUri());
        formData.add("client_secret", type.getClientSecret());
        formData.add("client_id", type.getClientId());
        return formData;
    }

    private User getUserProfile(SocialType socialType, OAuth2TokenResponseDto token, ClientRegistration type) throws JsonProcessingException {
        Map<String, Object> userAttributes = getUserAttributes(type, token);
        OAuth2UserInfoDto oAuth2UserInfoDto = OAuth2UserInfoFactory.getOAuth2UserInfo(socialType, userAttributes);

        return saveOrUpdate(socialType, oAuth2UserInfoDto);
    }

    private User saveOrUpdate(SocialType socialType, OAuth2UserInfoDto oAuth2UserInfoDto) {
        Optional<User> user = userRepository.findBySocialTypeAndSocialId(socialType, oAuth2UserInfoDto.getOAuth2Id());

        if (user.isEmpty()) {
            user = Optional.ofNullable(User.createUser(oAuth2UserInfoDto.getEmail(), socialType, oAuth2UserInfoDto.getOAuth2Id()));
            userRepository.save(user.get());
        } else {
//            user.update(oAuth2UserInfoDto);
        }

        return user.get();
    }

    private Map<String, Object> getUserAttributes(ClientRegistration type, OAuth2TokenResponseDto token) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer " + token.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<?> request =
                new HttpEntity<>(null, headers);

        RestTemplate restTemplate = new RestTemplate();

        String responseBody = restTemplate.exchange(
                type.getProviderDetails().getUserInfoEndpoint().getUri(),
                HttpMethod.GET,
                request,
                String.class
        ).getBody();

        return OBJECT_MAPPER.readValue(responseBody,Map.class);
    }

}
