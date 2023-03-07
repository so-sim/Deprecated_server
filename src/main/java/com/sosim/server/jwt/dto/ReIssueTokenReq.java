package com.sosim.server.jwt.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReIssueTokenReq {

//    @NotNull
//    private String accessToken;

    @NotNull
    private String refreshToken;
}
