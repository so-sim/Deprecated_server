package com.sosim.server.oauth;

import com.sosim.server.jwt.JwtFactory;
import com.sosim.server.oauth.dto.OAuth2TokenResponseDto;
import com.sosim.server.oauth.dto.OAuth2UserInfoDto;
import com.sosim.server.user.User;
import com.sosim.server.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OAuth2Service {
    private final InMemoryClientRegistrationRepository inMemoryRepository;
    private final UserRepository userRepository;
    private final JwtFactory jwtFactory;

    @Transactional
    public String login(Provider provider, String authorizationCode) {
        ClientRegistration type = inMemoryRepository.findByRegistrationId(provider.name().toLowerCase());
        OAuth2TokenResponseDto oAuth2Token = getToken(type, authorizationCode);
        User user = getUserProfile(provider, oAuth2Token, type);
        String accessToken = jwtFactory.createAccessToken(String.valueOf(user.getId()), user.getEmail());
        return accessToken;
    }

    private OAuth2TokenResponseDto getToken(ClientRegistration type, String authorizationCode) {
        return WebClient.create()
                .post()
                .uri(type.getProviderDetails().getTokenUri())
                .headers(httpHeaders -> {
                    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    httpHeaders.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest(authorizationCode, type))
                .retrieve()
                .bodyToMono(OAuth2TokenResponseDto.class)
                .block();
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

    private User getUserProfile(Provider provider, OAuth2TokenResponseDto token, ClientRegistration type) {
        Map<String, Object> userAttributes = getUserAttributes(type, token);
        OAuth2UserInfoDto oAuth2UserInfoDto = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, userAttributes);

        return saveOrUpdate(provider, oAuth2UserInfoDto);
    }

    private User saveOrUpdate(Provider provider, OAuth2UserInfoDto oAuth2UserInfoDto) {
        User user = userRepository.findByProviderAndSocialId(provider, oAuth2UserInfoDto.getOAuth2Id()).get();

        if (user == null) {
            user = User.createUser(oAuth2UserInfoDto.getEmail(), provider, oAuth2UserInfoDto.getOAuth2Id());
            userRepository.save(user);
        } else {
//            user.update(oAuth2UserInfoDto);
        }

        return user;
    }

    private Map<String, Object> getUserAttributes(ClientRegistration type, OAuth2TokenResponseDto token) {
        return WebClient.create()
                .get()
                .uri(type.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {})
                .block();
    }

}
