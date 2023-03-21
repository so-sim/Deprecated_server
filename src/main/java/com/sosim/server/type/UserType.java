package com.sosim.server.type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserType {

    WITHDRAWAL("탈퇴"),
    DORMANT("휴면"),
    USING("사용중");

    private String desc;
}
