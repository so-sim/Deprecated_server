package com.sosim.server.redis;

import com.sosim.server.jwt.JwtFactory;
import com.sosim.server.jwt.util.property.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
@SpringBootTest(properties = "spring.config.location=" +
    "classpath:/application.yml" +
    ",classpath:/application-jwt.yml"
)
@Slf4j
class RedisTemplateTest {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private JwtFactory jwtFactory;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    // redis 동작 테스트
    @Test
    void redisTest() {
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
        log.info("redis RefreshTokenKey : {}",  valueOperations.get(refreshTokenKey));
    }

}
