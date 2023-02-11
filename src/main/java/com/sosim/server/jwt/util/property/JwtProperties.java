package com.sosim.server.jwt.util.property;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class JwtProperties {

    public JwtProperties() {
    }

    public JwtProperties(String secretKey, Long accessTokenExpirationPeriod, Long refreshTokenExpirationPeriod,
        String accessHeader, String refreshHeader) {
        this.secretKey = secretKey;
        this.accessTokenExpirationPeriod = accessTokenExpirationPeriod;
        this.refreshTokenExpirationPeriod = refreshTokenExpirationPeriod;
        this.accessHeader = accessHeader;
        this.refreshHeader = refreshHeader;
    }

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;
}
