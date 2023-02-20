package com.sosim.server.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// TODO redis 애들 왔다갔다 하는데 쓸건데 Entity로 두는게 의미가 있을까? 근데 Redis 예제들 보면 모델에 또 있긴 있엇떤거같은데,,
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    private String userId;

    private String userEmail;

    private String refreshToken;

}
