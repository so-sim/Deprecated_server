package com.sosim.server.jwt;

import com.sosim.server.jwt.dao.JwtDao;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface JwtService {

    Map<HttpServletRequest, HttpServletResponse> verifyRefreshTokenAndReIssueAccessToken(HttpServletRequest request, HttpServletResponse response, String refreshToken);
    void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken);
    void saveRefreshToken();
    String refreshRefreshToken(JwtDao jwtDao);
}
