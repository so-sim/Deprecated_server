package com.sosim.server.event.dto.req;

import com.sosim.server.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
public class EventModifyReq {

    private String userName;

    @Setter
    private User user;

    private String groundsDate;

    private Long payment;

    private String grounds;

    private String paymentType;
}
