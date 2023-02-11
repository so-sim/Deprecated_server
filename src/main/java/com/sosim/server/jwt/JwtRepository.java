package com.sosim.server.jwt;

import com.sosim.server.jwt.dao.JwtDao;
import java.util.Optional;

// TODO redis repository
public interface JwtRepository {

    Optional<JwtDao> findByRefreshToken(String refreshToken);
}
