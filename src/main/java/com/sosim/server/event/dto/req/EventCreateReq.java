package com.sosim.server.event.dto.req;

import java.time.LocalDateTime;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EventCreateReq {

    // 팀원
    @NotEmpty
    private String userName;

    // 사유 발생 날짜
    @NotNull
    private LocalDateTime groundsDate;

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
