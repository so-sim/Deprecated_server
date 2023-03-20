package com.sosim.server.event.dto.req;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sosim.server.common.converter.StringToLocalDateTimeConverter;
import java.time.LocalDateTime;
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
    @JsonDeserialize(converter = StringToLocalDateTimeConverter.class)
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
