package com.sosim.server.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sosim.server.jwt.property.JwtProperties;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class JwtFactoryImpl implements JwtFactory {

    private final JwtProperties jwtProperties;

    @Override
    public String createAccessToken(String id) {
        Date now = new Date();
        return JWT.create()
            .withSubject(id)
            .withExpiresAt(new Date(now.getTime() + jwtProperties.getAccessTokenExpirationPeriod()))
            .sign(Algorithm.HMAC512(jwtProperties.getSecretKey()));
    }

    @Override
    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
            .withExpiresAt(new Date(now.getTime() + jwtProperties.getRefreshTokenExpirationPeriod()))
            .sign(Algorithm.HMAC512(jwtProperties.getSecretKey()));
    }
}
