package com.sosim.server.event.dto.info;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sosim.server.common.converter.LocalDateTimeToStringConverter;
import com.sosim.server.event.Event;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class EventInfo {

    @Setter
    private String userName;

    private Long payment;

    @JsonSerialize(converter = LocalDateTimeToStringConverter.class)
    private LocalDateTime groundsDate;

    private String paymentType;

    private String grounds;

    public static EventInfo from(Event event) {
        return EventInfo.builder()
            .payment(event.getPayment())
            .groundsDate(event.getGroundsDate())
            .paymentType(event.getPaymentType().getParam())
            .grounds(event.getGrounds())
            .build();
    }
}
