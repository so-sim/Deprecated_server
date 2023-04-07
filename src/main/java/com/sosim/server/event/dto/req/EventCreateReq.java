package com.sosim.server.event.dto.req;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;

@Getter
public class EventCreateReq {

    @NotNull
    private long groupId;

    @NotEmpty
    private String userName;

    @NotEmpty
    private String groundsDate;

    @NotNull
    private Long payment;

    @Size(max=65)
    private String grounds;

    @NotNull
    private String paymentType;
}
