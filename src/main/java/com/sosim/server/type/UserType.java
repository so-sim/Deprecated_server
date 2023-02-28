package com.sosim.server.type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserType {

    // 탈퇴
    WITHDRAWAL("withdrawal"),
    // 휴면
    DORMANT("dormant"),
    // 유효
    ACTIVE("active");

    private String desc;
}
