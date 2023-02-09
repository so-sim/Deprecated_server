package com.sosim.server.jwt;

public interface JwtService {

    String refresh(String refreshToken);
}
