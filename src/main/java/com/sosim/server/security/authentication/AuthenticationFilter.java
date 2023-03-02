package com.sosim.server.security.authentication;

import static com.sosim.server.jwt.util.constant.CustomConstant.NO_CHECK_URL;

import com.sosim.server.jwt.JwtProvider;
import com.sosim.server.jwt.JwtService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sosim.server.security.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        //TODO 이부분도 따로 메서드로 뺄지?
//        String refreshToken = jwtProvider.extractRefreshToken(request)
//            .filter(jwtProvider::isTokenValid)
//            .orElse(null);
//
//        // 요청헤더에 RefreshToken이 존재했다면, AccessToken이 만료된것
//        // RefreshToken을 redis의 것과 비교, 일치시 AccessToken 재발급
//        if (refreshToken != null) {
//            jwtService.verifyRefreshTokenAndReIssueAccessToken(request, response, refreshToken);
//            filterChain.doFilter(request, response);
//            return; // AccessToken을 재발급하고 인증 처리 방지를 위해 return으로 필터 진행 막기
//        }
//
//        // RefreshToken이 없거나 유효하지 않다면, AccessToken을 검사하고 인증을 처리하는 로직 수행
//        // AccessToken이 없거나 유효하지 않다면, 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가기 때문에 403 에러 발생
//        // AccessToken이 유효하다면, 인증 객체가 담긴 상태로 다음 필터로 넘어가기 때문에 인증 성공
//        if (refreshToken == null) {
////            checkAccessTokenAndAuthentication(request, response, filterChain);
//        }
//
//        filterChain.doFilter(request, response);

        // 사용자의 요청에 존재하는 Authorization Header Value
        // 1. 엑세스 토큰 들고 온 유저들만 검사
        // 2. 리프레쉬 토큰은 검사 필요 X
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if(authorizationHeader == null){
            filterChain.doFilter(request, response);
            return;
        }

        String token = getToken(authorizationHeader);

        setAuthToSecurityContextHolder(token);

        filterChain.doFilter(request, response);
    }

    private String getToken(String authorizationHeader){
        return authorizationHeader.substring(BEARER_PREFIX.length());
    }

    private void setAuthToSecurityContextHolder(String token) {
        // Security Context Holder 에 유저 정보 저장 메서드
        String userId = jwtProvider.extractId(token);
        AuthUser context = AuthUser.builder().id(userId).build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(context, null, context.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

