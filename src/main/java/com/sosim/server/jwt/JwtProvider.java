package com.sosim.server.jwt;

public interface JwtProvider {

    String verifyAccessToken(String accessToken);
    String verifyRefreshToken(String accessToken);
    String refresh(String refreshToken);

}
