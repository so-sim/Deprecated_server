package com.sosim.server.oauth.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sosim.server.jwt.RefreshToken;
import com.sosim.server.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonIgnore
    private RefreshToken refreshToken;

    public static LoginResponse createOAuth2JwtResponseDto(User user, String accessToken, String refreshToken) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(createRefreshToken(user, refreshToken))
                .build();
    }

    private static RefreshToken createRefreshToken(User user, String refreshToken) {
        return RefreshToken.builder()
                .id(String.valueOf(user.getId()))
                .refreshToken(refreshToken)
                .build();
    }
}
