package com.sosim.server.jwt;

public class JwtProviderImpl implements JwtProvider {

    // repository 접근이 없는 작업들 - jwt해석, 검증

    @Override
    public String verifyAccessToken(String accessToken) {
        return null;
    }

    @Override
    public String verifyRefreshToken(String accessToken) {
        return null;
    }

    @Override
    public String refresh(String refreshToken) {
        return null;
    }
}
