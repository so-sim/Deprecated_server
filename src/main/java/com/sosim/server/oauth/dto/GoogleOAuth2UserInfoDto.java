package com.sosim.server.oauth.dto;

import java.util.Map;

public class GoogleOAuth2UserInfoDto extends OAuth2UserInfoDto {

    public GoogleOAuth2UserInfoDto(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
}
