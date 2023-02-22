package com.sosim.server.jwt;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface JwtProvider {

    Optional<String> extractAccessToken(HttpServletRequest request);
    Optional<String> extractRefreshToken(HttpServletRequest request);
    Optional<String> extractId(String AccessToken);
    boolean isTokenValid(String token);
    String reIssueRefreshToken(String id);
    String reIssueRefreshToken(RefreshToken refreshToken);
    String verifyAccessToken(String accessToken);
    String refresh(String refreshToken);
    void setAccessTokenHeader(HttpServletResponse response, String accessToken);
    void setRefreshTokenHeader(HttpServletResponse response, String refreshToken);

}
