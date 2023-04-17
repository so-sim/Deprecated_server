package com.sosim.server.jwt;

public interface JwtProvider {

    String extractId(String AccessToken);
    boolean checkRenewRefreshToken(String refreshToken);
}
