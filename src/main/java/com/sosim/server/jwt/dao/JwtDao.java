package com.sosim.server.jwt.dao;

import static com.sosim.server.jwt.util.constant.CustomConstant.REFRESH_TOKEN_KEY;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class JwtDao {
    private final RedisTemplate<String, String> redisTemplate;
    private String refreshToken;


    public JwtDao(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setHashes(String refreshToken, String id, String email) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        Map<String, Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add(id);
        list.add(email);
        map.put(refreshToken, list);
        hashOperations.put(REFRESH_TOKEN_KEY, refreshToken, map);
    }

    public void setHashes(String refreshToken, String id, String email, Duration duration) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        Map<String, Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add(id);
        list.add(email);
        map.put(refreshToken, list);
        hashOperations.put(REFRESH_TOKEN_KEY, refreshToken, map);
    }

    public void setValues(String key, String data) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data);
    }

    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    public String getValues(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    // TODO 이게 맞나? 뭔가 이상함을 느끼고있음..테스트 필요
    public List<String> getHashes(String key, String hashKey) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        List<String> list = (List) hashOperations.get(key, hashKey);
        return list;
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
}
