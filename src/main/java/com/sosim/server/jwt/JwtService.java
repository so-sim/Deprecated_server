package com.sosim.server.jwt;

import com.sosim.server.jwt.dto.ReIssueTokenInfo;
import com.sosim.server.user.User;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface JwtService {

    void saveRefreshToken(RefreshToken refreshToken);
    ReIssueTokenInfo verifyRefreshTokenAndReIssueAccessToken(HttpServletRequest httpServletRequest, HttpServletResponse response);
    void sendRefreshToken(HttpServletResponse response, String refreshToken);
    void setRefreshTokenHeader(HttpServletResponse response, String refreshToken);
}
