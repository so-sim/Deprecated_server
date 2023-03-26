package com.sosim.server.event.dto.req;

import com.sosim.server.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
public class EventModifyReq {

    // 팀원
    private String userName;

    @Setter
    private User user;

    // 사유 발생 날짜
    private String groundsDate;

    // 금액
    private Long payment;

    // 사유
    private String grounds;

    //  납부여부
    private String paymentType;
}
