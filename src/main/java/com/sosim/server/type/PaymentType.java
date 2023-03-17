package com.sosim.server.type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PaymentType {

    NON_PAYMENT("미납", "non"),
    CONFIRMING("확인중/확인필요", "con"),
    FULL_PAYMENT("완납", "full");

    private String desc;
    private String param;
}
