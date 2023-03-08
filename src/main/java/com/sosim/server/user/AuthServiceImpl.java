package com.sosim.server.user;

import com.sosim.server.security.AuthUser;
import org.springframework.security.core.Authentication;

public class AuthServiceImpl implements AuthService{

    //TODO 잘 되나 테스트 필요
    @Override
    public AuthUser getSessionInfo(Authentication authentication) {
        return (AuthUser) authentication.getCredentials();
    }
}
