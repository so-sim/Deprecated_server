package com.sosim.server.jwt;

import com.sosim.server.jwt.dao.RedisUser;
import org.springframework.data.repository.CrudRepository;

public interface RedisUserRepository extends CrudRepository<RedisUser, String> {

}
