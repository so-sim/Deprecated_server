package com.sosim.server.oauth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sosim.server.jwt.RefreshToken;
import com.sosim.server.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuth2JwtResponseDto {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonIgnore
    private RefreshToken refreshToken;

    public static OAuth2JwtResponseDto createOAuth2JwtResponseDto(User user, String accessToken, String refreshToken) {
        return OAuth2JwtResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(createRefreshToken(user, refreshToken))
                .build();
    }

    private static RefreshToken createRefreshToken(User user, String refreshToken) {
        return RefreshToken.builder()
                .userId(String.valueOf(user.getId()))
                .userEmail(user.getEmail())
                .refreshToken(refreshToken)
                .build();
    }
}
