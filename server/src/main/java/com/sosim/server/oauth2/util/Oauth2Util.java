package com.sosim.server.oauth2.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sosim.server.oauth2.dto.OauthProfileDto;
import com.sosim.server.oauth2.dto.OauthTokenDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class Oauth2Util {
    private static final int GET_TOKEN = 1;
    private static final int GET_PROFILE = 2;

    private ObjectMapper objectMapper;

    public Oauth2Util(ObjectMapper objectMapper) { // 다시 한번 정확한 의미 찾아보기
        this.objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    public OauthTokenDto getToken(String code) throws JsonProcessingException {
        HttpEntity<?> request = createHttpEntity(GET_TOKEN, code);
        return readValue("https://kauth.kakao.com/oauth/token", request, OauthTokenDto.class);
    }

    public OauthProfileDto getProfile(String accessToken) throws JsonProcessingException {
        HttpEntity<?> request = createHttpEntity(GET_PROFILE, accessToken);
        return readValue("https://kapi.kakao.com/v2/user/me", request, OauthProfileDto.class);
    }

    private HttpEntity<?> createHttpEntity(int kind, String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<?> request = null;

        if (kind == GET_TOKEN) {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", "f17f6d56ad837b715fd34160ac7b21f4");
            params.add("redirect_uri", "http://localhost:8080/oauth/kakao/callback");
            params.add("code", code);
            params.add("client_secret", "hQFRF2bx9LQyrP3CErKUMVE7MJAEBpmI");

            request = new HttpEntity<>(params, headers);
        }

        if (kind == GET_PROFILE) {
            headers.add("Authorization", "Bearer " + code);
            request = new HttpEntity<>(null, headers);
        }

        return request;
    }

    private <T>T readValue(String url, HttpEntity<?> request, Class<T> type) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String responseBody = restTemplate.exchange(url, HttpMethod.POST, request, String.class).getBody();
        return objectMapper.readValue(responseBody, type);
    }
}
