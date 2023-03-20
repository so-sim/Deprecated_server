package com.sosim.server.event.dto.info;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sosim.server.common.converter.LocalDateTimeToStringConverter;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public class EventInfo {

    // 팀원이름
    private String userName;

    // 금액
    private Long payment;

    // 사유 발생 날짜
    @JsonSerialize(converter = LocalDateTimeToStringConverter.class)
    private LocalDateTime groundsDate;

    // 납부여부
    private String paymentType;

    // 사유
    private String grounds;
}
