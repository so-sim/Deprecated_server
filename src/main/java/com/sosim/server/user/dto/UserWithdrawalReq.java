package com.sosim.server.user.dto;

import com.sosim.server.type.WithdrawalGroundsType;
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
    private LocalDateTime withdrawalDate;

    @NotNull
    private WithdrawalGroundsType withdrawalGroundsType;
}
