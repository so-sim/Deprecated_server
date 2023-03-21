package com.sosim.server.oauth;

import com.sosim.server.oauth.dto.request.GoogleUserInfoRequest;
import com.sosim.server.oauth.dto.request.KakaoUserInfoRequest;
import com.sosim.server.oauth.dto.request.OAuth2UserInfoRequest;
import com.sosim.server.type.SocialType;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfoRequest getOAuth2UserInfo(SocialType socialType, Map<String, Object> attributes) {
        switch (socialType) {
            case GOOGLE: return new GoogleUserInfoRequest(attributes);
//            case NAVER: return new NaverOAuth2UserInfo(attributes);
            case KAKAO: return new KakaoUserInfoRequest(attributes);
            default: throw new IllegalArgumentException("Invalid Social Type.");
        }
    }
}