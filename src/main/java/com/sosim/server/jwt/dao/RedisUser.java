package com.sosim.server.jwt.dao;

import javax.persistence.Id;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

// RedisRepository를 사용한 객체 저장방법
@Setter
@RedisHash("user")
public class RedisUser {

    @Id
    private String id;
    private String name;
    private int age;

    public RedisUser(String name, int age) {
        this.name = name;
        this.age = age;
    }

}
