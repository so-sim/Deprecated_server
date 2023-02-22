package com.sosim.server.jwt.dao;

import static com.sosim.server.jwt.util.constant.CustomConstant.ID;
import static com.sosim.server.jwt.util.constant.CustomConstant.SOCIAL_ID;
import static com.sosim.server.jwt.util.constant.CustomConstant.SOCIAL_TYPE;

import com.sosim.server.jwt.RefreshToken;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class JwtDao {
    private final RedisTemplate<String, String> redisTemplate;

    public JwtDao(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setHashes(RefreshToken refreshToken) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        Map<String, Object> map = new HashMap<>();
        map.put(ID, refreshToken.getId());
        map.put(SOCIAL_TYPE, refreshToken.getSocialType());
        map.put(SOCIAL_ID, refreshToken.getSocialId());
        hashOperations.putAll(refreshToken.getRefreshToken(), map);
    }

    public void setHashes(RefreshToken refreshToken, Duration duration) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        Map<String, Object> map = new HashMap<>();
        map.put(ID, refreshToken.getId());
        map.put(SOCIAL_TYPE, refreshToken.getSocialType());
        map.put(SOCIAL_ID, refreshToken.getSocialId());
        hashOperations.putAll(refreshToken.getRefreshToken(), map);
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

    public Map<String, String> getHashes(String refreshToken) {
        Map<String, String> map = new HashMap<>();
        String id = (String) redisTemplate.opsForHash().get(refreshToken, ID);
        String socialType = (String) redisTemplate.opsForHash().get(refreshToken, SOCIAL_TYPE);
        String socialId = (String) redisTemplate.opsForHash().get(refreshToken, SOCIAL_ID);
        map.put(ID, id);
        map.put(SOCIAL_TYPE, socialType);
        map.put(SOCIAL_ID, socialId);
        return map;
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
}
