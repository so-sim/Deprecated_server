package com.sosim.server.oauth.dto;

import com.sosim.server.oauth.Provider;

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
    public String getNickname() {
        return (String) attributes.get("name");
    }

    @Override
    public Provider getOAuth2Provider() {
        return Provider.GOOGLE;
    }
}
