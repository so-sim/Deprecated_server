package com.sosim.server.oauth.dto;

import com.sosim.server.oauth.SocialType;

import java.util.Map;

public class GoogleOAuth2UserInfoDto extends OAuth2UserInfoDto {

    public GoogleOAuth2UserInfoDto(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getOAuth2Id() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public SocialType getOAuth2SocialType() {
        return SocialType.GOOGLE;
    }
}
