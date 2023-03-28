package com.sosim.server.jwt;

import static com.sosim.server.jwt.constant.CustomConstant.NONE;
import static com.sosim.server.jwt.constant.CustomConstant.REFRESH_TOKEN;
import static com.sosim.server.jwt.constant.CustomConstant.SET_COOKIE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sosim.server.config.exception.CustomException;
import com.sosim.server.jwt.dao.JwtDao;
import com.sosim.server.jwt.dto.ReIssueTokenInfo;
import com.sosim.server.jwt.property.JwtProperties;
import com.sosim.server.security.AuthUser;
import com.sosim.server.type.CodeType;
import com.sosim.server.user.User;
import com.sosim.server.user.UserRepository;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtServiceImpl implements JwtService{
    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;
    private final JwtFactory jwtFactory;
    private final JwtProvider jwtProvider;
    private final JwtDao jwtDao;
    private final ObjectMapper objectMapper;

    /**
     * refreshToken redis에 저장
     */
    @Override
    public void saveRefreshToken(RefreshToken refreshToken) {
        jwtDao.setValues(refreshToken.getRefreshToken(), refreshToken.getId());
    }

    /**
     *  1. 헤더에서 추출한 RefreshToken으로 redis에서 유저 정보를 탐색
     *  2. 유저가 있다면 AccessToken 생성, refreshToken 재발급 & redis에 refreshToken 업데이트
     *  3. AccessToken 문자열과 Cookie에 담아 응답 헤더에 실은 RefreshToken값 반환
     */
    @Override
    public ReIssueTokenInfo verifyRefreshTokenAndReIssueAccessToken(HttpServletRequest httpServletRequest, HttpServletResponse response) {

        String refreshToken = httpServletRequest.getHeader(SET_COOKIE);
        String id = jwtDao.getValues(refreshToken);
        log.info("refreshToken : {}, id: {}", refreshToken, id);
        User user = userRepository.findById(Long.parseLong(id)).orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_USER));
        if(Long.parseLong(id) != user.getId()) {
            log.info("invalid user");
            throw new CustomException(CodeType.INVALID_USER);
        }
        jwtDao.deleteValues(refreshToken);
        String reIssuedRefreshToken = jwtProvider.reIssueRefreshToken(id);
        sendRefreshToken(response, reIssuedRefreshToken);
        return ReIssueTokenInfo.builder().accessToken(jwtFactory.createAccessToken(id)).build();
    }

    @Override
    public void sendRefreshToken(HttpServletResponse response, String refreshToken) {

        setRefreshTokenHeader(response, refreshToken);
        log.info("Refresh Token 헤더 설정 완료");
    }

    @Override
    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        log.info("checkAccessTokenAndAuthentication() 호출");
        jwtProvider.extractAccessToken(request)
            .filter(jwtProvider::isTokenValid)
            .ifPresent(accessToken -> jwtProvider.extractId(accessToken)
                .ifPresent(id -> userRepository.findById(Long.parseLong(id))
                    .ifPresent(this::saveAuthentication)));

        filterChain.doFilter(request, response);
    }

    @Override
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {

        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN , refreshToken)
            .maxAge(jwtProperties.getRefreshTokenMaxAge())
            .secure(true)
            .sameSite(NONE)
            .httpOnly(true)
            .build();

        response.setHeader(SET_COOKIE, cookie.toString());
    }

    public void saveAuthentication(User user) {

        AuthUser context = AuthUser.builder().id(String.valueOf(user.getId())).build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(context, null, context.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
