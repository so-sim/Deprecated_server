package com.sosim.server.jwt;

import com.sosim.server.jwt.dao.JwtDao;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

// TODO redis repository
public interface JwtRepository extends CrudRepository<JwtDao, String> {

    Optional<JwtDao> findByRefreshToken(String refreshToken);
}
