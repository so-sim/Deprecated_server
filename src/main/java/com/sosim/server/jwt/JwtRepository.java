package com.sosim.server.jwt;

import com.sosim.server.jwt.dao.JwtDao;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

// TODO 현재 쓰이지 않고 있음, 나중에 결국 안쓰게되면 지울것
public interface JwtRepository extends CrudRepository<JwtDao, String> {

    Optional<JwtDao> findByRefreshToken(String refreshToken);
}
