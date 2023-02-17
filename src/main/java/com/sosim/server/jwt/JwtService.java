package com.sosim.server.jwt;

import com.sosim.server.jwt.dao.JwtDao;
import javax.servlet.http.HttpServletResponse;

public interface JwtService {


    void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken);
    void saveRefreshToken(String accessToken);
    String verifyRefreshToken(HttpServletResponse response, String refreshToken);
    String refreshRefreshToken(JwtDao jwtDao);
}
