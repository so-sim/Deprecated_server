package com.sosim.server.type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum EventType {

    DUES_PAYMENT("벌금 납부"),
    USE_PAYMENT("벌금 사용");

    private String desc;
}
