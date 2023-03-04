package com.sosim.server.jwt.dto;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReIssueTokenInfo {

    @NotNull
    private String accessToken;

    @NotNull
    private HttpServletResponse response;
}
