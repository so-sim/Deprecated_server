package com.sosim.server.jwt;

import java.util.Date;
import org.springframework.beans.factory.annotation.Value;


public class JwtFactoryImpl implements JwtFactory {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    // TODO 확인 : claim을 providerId로 처리하려고 하는데, interface정의 당시 혹시 User의 pk id를 의미한건지?
    // 해당 부분은 회의때(김진하 본인이 long을 원하긴 했지만)long 타입으로 정의하기로 협의함 -> 바꿔도 상관없긴한데, 일단 현재는 그러함
    @Override
    public String createAccessToken(String userId) {
        Date now = new Date();
        return JWT.create()
            .withSubject(ACCESS_TOKEN_SUBJECT)
            .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
            .withClaim(EMAIL_CLAIM, email)
            .sign(Algorithm.HMAC512(secretKey));
    }

    @Override
    public String createRefreshToken(String accessToken) {
        return null;
    }
}
