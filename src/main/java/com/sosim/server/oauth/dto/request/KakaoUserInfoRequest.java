package com.sosim.server.oauth.dto.request;

import com.sosim.server.oauth.dto.request.OAuth2UserInfoRequest;
import com.sosim.server.type.SocialType;

import java.util.Map;

public class KakaoUserInfoRequest extends OAuth2UserInfoRequest {

    private Map<String, Object> kakaoAccountAttributes;

    public KakaoUserInfoRequest(Map<String, Object> attributes) {
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
    public SocialType getOAuth2SocialType() {
        return SocialType.KAKAO;
    }
}