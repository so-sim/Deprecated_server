package com.sosim.server.event.dto.info;

import java.time.LocalDateTime;

public class EventInfo {

    // 팀원이름
    private String userName;

    // 금액
    private Long payment;

    // 사유 발생 날짜
    private LocalDateTime groundsDate;

    // 납부여부
    private String paymentType;

    // 사유
    private String grounds;
}
