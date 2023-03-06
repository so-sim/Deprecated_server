package com.sosim.server.jwt;

import static com.sosim.server.jwt.constant.CustomConstant.BEARER;
import static com.sosim.server.jwt.constant.CustomConstant.ID;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sosim.server.jwt.dao.JwtDao;
import com.sosim.server.jwt.property.JwtProperties;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


// repository 접근이 없는 작업들 - jwt해석, 검증
@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtProviderImpl implements JwtProvider {

    private final JwtProperties jwtProperties;
    private final JwtFactory jwtFactory;
    private final JwtDao jwtDao;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(jwtProperties.getAccessHeader()))
            .filter(refreshToken -> refreshToken.startsWith(BEARER))
            .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    @Override
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(jwtProperties.getRefreshHeader()))
            .filter(refreshToken -> refreshToken.startsWith(BEARER))
            .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(jwtProperties.getSecretKey())).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }

    /**
     * 리프레시 토큰 재발급 + Redis에 DB에 재발급한 리프레시 토큰 업데이트
     */
    // 1.
    @Override
    public String reIssueRefreshToken(String id) {
        String reIssuedRefreshToken = jwtFactory.createRefreshToken();
        // TODO Duration 사용 고려해서 다시 -> 추후에 진행하기로
        jwtDao.setValues(reIssuedRefreshToken, id);
        return reIssuedRefreshToken;
    }

    /**
     * AccessToken에서 Id추출
     * 추출 전에 JWT.require()로 검증기 생성
     * verify로 AceessToken 검증 후
     * 유효하다면 getClaim()으로 Id 추출
     * 유효하지 않다면 빈 Optional 객체 반환
     */
    @Override
    public Optional<String> extractId(String accessToken) {
        try {
            // 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(jwtProperties.getSecretKey()))
                .build() // 반환된 빌더로 JWT verifier 생성
                .verify(accessToken) // accessToken을 검증하고 유효하지 않다면 예외 발생
                .getClaim(ID) // claim(id) 가져오기
                .asString());
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }
}
