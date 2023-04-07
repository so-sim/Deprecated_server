package com.sosim.server.jwt;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

public interface JwtProvider {

    Optional<String> extractAccessToken(HttpServletRequest request);
    Optional<String> extractRefreshToken(HttpServletRequest request);
    String extractId(String AccessToken);
    boolean isTokenValid(String token);
    String reIssueRefreshToken(String id);

}
