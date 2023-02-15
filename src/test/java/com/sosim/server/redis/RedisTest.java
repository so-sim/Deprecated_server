package com.sosim.server.redis;

import com.sosim.server.jwt.JwtFactory;
import com.sosim.server.jwt.JwtFactoryImpl;
import com.sosim.server.jwt.util.property.JwtProperties;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

class RedisTemplateTest {
    JwtProperties jwtProperties = new JwtProperties();

    JwtFactory jwtFactory = new JwtFactoryImpl(jwtProperties);
    RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();


    // redis 동작 테스트
    @Test
    void test1() {
        // given
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String accessToken = jwtFactory.createAccessToken("1");
        String refreshToken = jwtFactory.createRefreshToken(accessToken);
        String refreshTokenKey = "refreshTokenKey";

        // when
        valueOperations.set(refreshTokenKey, refreshToken);

        // then
        String value = valueOperations.get(refreshTokenKey);
        Assertions.assertThat(value).isEqualTo(refreshToken);
        System.out.println("redis RefreshTokenKey : " + valueOperations.get(refreshTokenKey));
    }

}
