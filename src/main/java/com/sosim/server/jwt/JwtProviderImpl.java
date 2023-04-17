package com.sosim.server.jwt;

import static com.sosim.server.jwt.constant.CustomConstant.ID;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sosim.server.config.exception.CustomException;
import com.sosim.server.jwt.property.JwtProperties;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.sosim.server.type.CodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


// repository 접근이 없는 작업들 - jwt해석, 검증
@Service
@RequiredArgsConstructor
@Getter
public class JwtProviderImpl implements JwtProvider {

    private final JwtProperties jwtProperties;

    private DecodedJWT decodedJWT(String token) {
        try {
            return JWT.require(Algorithm.HMAC512(jwtProperties.getSecretKey())).build().verify(token);
        } catch (TokenExpiredException e) {
            throw new CustomException(CodeType.EXPIRE_TOKEN);
        } catch (JWTVerificationException e) {
            throw new CustomException(CodeType.FALSIFIED_TOKEN);
        }
    }

    @Override
    public boolean checkRenewRefreshToken(String refreshToken) {
        Instant expiredTime = decodedJWT(refreshToken).getExpiresAtAsInstant();

        return Instant.now().until(expiredTime, ChronoUnit.DAYS) < 3L;
    }

    /**
     * AccessToken에서 Id추출
     * 추출 전에 JWT.require()로 검증기 생성
     * verify로 AceessToken 검증 후
     * 유효하다면 getClaim()으로 Id 추출
     * 유효하지 않다면 빈 Optional 객체 반환
     */
    @Override
    public String extractId(String accessToken) {
        return decodedJWT(accessToken).getClaim(ID).asString();
    }
}
