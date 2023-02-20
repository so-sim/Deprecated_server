package com.sosim.server.security.authentication;

import static com.sosim.server.jwt.util.constant.CustomConstant.NO_CHECK_URL;

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
        // "/login"으로 들어오는 요청은 Filter 작동 X
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response); // "/login" 요청이 들어오면, 다음 필터 호출
            return; // return으로 이후 현재 필터 진행 막기 (안해주면 아래로 내려가서 계속 필터 진행시킴)
        }

        String refreshToken = jwtProvider.extractRefreshToken(request)
            .filter(jwtProvider::isTokenValid)
            .orElse(null);

        // 요청헤더에 RefreshToken이 존재했다면, AccessToken이 만료된것
        // RefreshToken을 redis의 것과 비교, 일치시 AccessToken 재발급
        if (refreshToken != null) {
            jwtService.verifyRefreshTokenAndReIssueAccessToken(request, response, refreshToken);
            filterChain.doFilter(request, response);
            return; // AccessToken을 재발급하고 인증 처리 방지를 위해 return으로 필터 진행 막기
        }

        // AccessToken을 검사하고 인증해서 AccessToken이 유효하다면, 인증 객체가 담긴 상태로 다음 필터로 넘어감
        // 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가면 403 에러 발생
        if (refreshToken == null) {
            jwtService.checkAccessTokenAndAuthentication(request, response, filterChain);
        }
    }
}

