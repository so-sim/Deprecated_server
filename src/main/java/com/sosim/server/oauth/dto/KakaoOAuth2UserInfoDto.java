package com.sosim.server.oauth.dto;

import com.sosim.server.oauth.Provider;

import java.util.Map;

public class KakaoOAuth2UserInfoDto extends OAuth2UserInfoDto {

    private Map<String, Object> kakaoAccountAttributes;

    public KakaoOAuth2UserInfoDto(Map<String, Object> attributes) {
        super(attributes);
        kakaoAccountAttributes = (Map<String, Object>) attributes.get("kakao_account");
    }

    @Override
    public String getOAuth2Id() {
        return String.valueOf(super.attributes.get("id"));
    }

    @Override
    public String getEmail() {
        return (String) kakaoAccountAttributes.get("email");
    }

    @Override
    public Provider getOAuth2Provider() {
        return Provider.KAKAO;
    }
}