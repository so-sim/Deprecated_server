package com.sosim.server.oauth.dto.request;

import com.sosim.server.oauth.dto.request.OAuth2UserInfoRequest;
import com.sosim.server.type.SocialType;

import java.util.Map;

public class GoogleUserInfoRequest extends OAuth2UserInfoRequest {

    public GoogleUserInfoRequest(Map<String, Object> attributes) {
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
