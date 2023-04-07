package com.sosim.server.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.sosim.server.config.exception.CustomException;
import com.sosim.server.jwt.JwtProvider;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sosim.server.security.AuthUser;
import com.sosim.server.type.CodeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }


        String accessToken = authorizationHeader.substring(BEARER.length());

        try {
            setAuthenticationPrincipal(jwtProvider.extractId(accessToken));
        } catch (TokenExpiredException e) {
            throw new CustomException(CodeType.EXPIRE_TOKEN);
        } catch (JWTVerificationException e) {
            throw new CustomException(CodeType.FALSIFIED_TOKEN);
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthenticationPrincipal(String userId) {
        AuthUser authUser = AuthUser.builder().id(userId).build();
        AbstractAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

