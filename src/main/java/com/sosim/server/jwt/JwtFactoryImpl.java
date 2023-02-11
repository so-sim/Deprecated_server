package com.sosim.server.jwt;

import static com.sosim.server.jwt.util.constant.CustomConstant.ACCESS_TOKEN_SUBJECT;
import static com.sosim.server.jwt.util.constant.CustomConstant.PROVIDER_ID_CLAIM;
import static com.sosim.server.jwt.util.constant.CustomConstant.REFRESH_TOKEN_SUBJECT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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

    @Override
    public String createAccessToken(String providerId) {
        Date now = new Date();
        return
            JWT.create()
            .withSubject(ACCESS_TOKEN_SUBJECT)
            .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
            .withClaim(PROVIDER_ID_CLAIM, providerId)
            .sign(Algorithm.HMAC512(secretKey));
    }

    @Override
    public String createRefreshToken(String accessToken) {
        Date now = new Date();
        return JWT.create()
            .withSubject(REFRESH_TOKEN_SUBJECT)
            .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
            .sign(Algorithm.HMAC512(secretKey));
    }
}
