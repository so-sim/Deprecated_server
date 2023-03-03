package com.sosim.server.jwt;

import com.sosim.server.user.User;
import java.io.IOException;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface JwtService {

    void saveRefreshToken(RefreshToken refreshToken);
    Map<String, HttpServletResponse> verifyRefreshTokenAndReIssueAccessToken(HttpServletRequest request, HttpServletResponse response, String refreshToken);
    void sendRefreshToken(HttpServletResponse response, String refreshToken);
    void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException;
    void saveAuthentication(User myUser);
    void setRefreshTokenHeader(HttpServletResponse response, String refreshToken);

}
