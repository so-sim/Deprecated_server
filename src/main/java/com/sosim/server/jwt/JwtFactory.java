package com.sosim.server.jwt;

/**
 * <h2>로그인 인증 토큰은 생성하는 객체</h2>
 */
public interface JwtFactory {
    String createAccessToken(String id, String email);
    String createRefreshToken();
}
