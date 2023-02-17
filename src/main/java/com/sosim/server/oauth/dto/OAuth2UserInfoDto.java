package com.sosim.server.oauth.dto;

import com.sosim.server.oauth.Provider;

import java.util.Map;

public abstract class OAuth2UserInfoDto {
    protected Map<String, Object> attributes;

    protected OAuth2UserInfoDto(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getOAuth2Id();

    public abstract String getEmail();

    public abstract String getNickname();

    public abstract Provider getOAuth2Provider();

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }
}