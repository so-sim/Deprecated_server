package com.sosim.server.event.dto.req;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;

@Getter
public class EventCreateReq {

    // 팀원
    @NotEmpty
    private String userName;

    // 사유 발생 날짜
    @NotEmpty
    private String groundsDate;

    // 금액
    @NotNull
    private Long payment;

    // 사유
    @NotEmpty
    @Size(max=65)
    private String grounds;

    //  납부여부
    @NotNull
    private String paymentType;
}
