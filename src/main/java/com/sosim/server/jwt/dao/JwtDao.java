package com.sosim.server.jwt.dao;

import java.time.Duration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class JwtDao {
    private final RedisTemplate<String, String> redisTemplate;

    public JwtDao(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setValues(String key, String id) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, id);
    }

    public void setValues(String key, String id, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, id, duration);
    }

    public String getValues(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    public void deleteValues(String refreshToken) {
        redisTemplate.delete(refreshToken);
    }
}
