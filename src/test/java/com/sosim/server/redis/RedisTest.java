package com.sosim.server.redis;

import static com.sosim.server.jwt.constant.CustomConstant.REFRESH_TOKEN_KEY;
import static org.assertj.core.api.Assertions.assertThat;

import com.sosim.server.jwt.JwtFactory;
import com.sosim.server.jwt.property.JwtProperties;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
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

    @Test
    void valueOperationsTest() {
        // given
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String refreshToken = jwtFactory.createRefreshToken();
        String refreshTokenKey = "refreshTokenKey";

        // when
        valueOperations.set(refreshTokenKey, refreshToken);

        // then
        String value = valueOperations.get(refreshTokenKey);
        assertThat(value).isEqualTo(refreshToken);
        log.info("redis RefreshTokenKey : {}",  valueOperations.get(refreshTokenKey));
    }

    // TODO
    @Test
    void createAccessTokenTest() {
        String accessToken = jwtFactory.createAccessToken("1", "kkk@sosim.com");
    }

    @Test
    void HashOperationsTest() {
        // given
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        Map<String, Object> map = new HashMap<>();

        // when
        map.put("id", "id1");
        map.put("email", "email1");
        map.put("refreshToken", "refreshToken1");
        hashOperations.putAll(REFRESH_TOKEN_KEY + "_1", map);

        // then
        String id = (String) redisTemplate.opsForHash().get(REFRESH_TOKEN_KEY + "_1", "id");
        assertThat(id).isEqualTo("id1");
        String email = (String) redisTemplate.opsForHash().get(REFRESH_TOKEN_KEY + "_1", "email");
        assertThat(email).isEqualTo("email1");
        String refreshToken = (String) redisTemplate.opsForHash().get(REFRESH_TOKEN_KEY + "_1", "refreshToken");
        assertThat(refreshToken).isEqualTo("refreshToken1");
    }
}
