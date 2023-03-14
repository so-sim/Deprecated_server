package com.sosim.server.oauth.dto.request;

import com.sosim.server.type.SocialType;

import java.util.Map;

public abstract class OAuth2UserInfoRequest {
    protected Map<String, Object> attributes;

    protected OAuth2UserInfoRequest(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getOAuth2Id();

    public abstract String getEmail();

    public abstract SocialType getOAuth2SocialType();

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }
}