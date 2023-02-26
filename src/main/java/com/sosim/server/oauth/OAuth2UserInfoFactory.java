package com.sosim.server.oauth;

import com.sosim.server.oauth.dto.GoogleOAuth2UserInfoDto;
import com.sosim.server.oauth.dto.KakaoOAuth2UserInfoDto;
import com.sosim.server.oauth.dto.OAuth2UserInfoDto;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfoDto getOAuth2UserInfo(Provider provider, Map<String, Object> attributes) {
        switch (provider) {
            case GOOGLE: return new GoogleOAuth2UserInfoDto(attributes);
//            case NAVER: return new NaverOAuth2UserInfo(attributes);
            case KAKAO: return new KakaoOAuth2UserInfoDto(attributes);
            default: throw new IllegalArgumentException("Invalid Social Type.");
        }
    }
}