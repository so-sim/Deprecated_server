package com.sosim.server.type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum StatusType {

    USING("사용중"),
    DELETED("삭제됨");

    private String desc;
}
