package com.sosim.server.security.authentication;

import com.sosim.server.jwt.JwtProvider;
import com.sosim.server.jwt.JwtService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtProvider.extractAccessToken(request)
            .filter(jwtProvider::isTokenValid)
            .orElse(null);

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtService.checkAccessTokenAndAuthentication(request, response, filterChain);
    }
}

