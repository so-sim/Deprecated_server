package com.sosim.server.oauth2.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OauthProfileDto {
    // 추후 받아올 사용자 정보 확정 후, refactoring
    private Long id;

    private Properties properties;

    private KakaoAccount kakaoAccount;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private static class Properties {
        private String nickname;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private static class KakaoAccount {
        private Profile profile;
//        private Gender gender;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private static class Profile {
        private String nickname;
    }
}
