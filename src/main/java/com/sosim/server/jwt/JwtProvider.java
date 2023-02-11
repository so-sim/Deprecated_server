package com.sosim.server.jwt;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

public interface JwtProvider {

    Optional<String> extractAccessToken(HttpServletRequest request);
    Optional<String> extractRefreshToken(HttpServletRequest request);
    boolean isTokenValid(String token);
    String verifyAccessToken(String accessToken);
    String refresh(String refreshToken);

}
