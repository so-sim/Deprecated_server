package com.sosim.server.user.dto.req;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserWithdrawalReq {

    @NotNull
    private long userId;

    private LocalDateTime withdrawalDate;

    @NotNull
    private int withdrawalGroundsType;
}
