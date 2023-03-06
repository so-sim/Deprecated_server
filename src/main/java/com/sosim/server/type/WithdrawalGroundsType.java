package com.sosim.server.type;

import com.sosim.server.common.util.EnumUtils;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum WithdrawalGroundsType {

    // 서비스를 잘 이용하지 않아서
    NOT_USING_SERVICE("not using service", 1),

    // 내가 생각한 서비스가 아니라서
    NOT_THE_SERVICE_WAS_THINKING_OF("not the service was thinking of", 2),

    // 사용하기 불편해서
    INCONVENIENT_TO_USE("inconvenient to use", 3),

    // 사용할 수 있는 기능이 부족해서
    LACK_OF_AVAILABLE_FEATURES("lack of available features", 4),

    // 새 계정을 만들고 싶어서
    WANT_TO_CREATE_A_NEW_ACCOUNT("want to create a new account", 5),

    // 서비스 사용 중 에러가 잦아서
    FREQUENT_ERRORS_WHILE_USING_THE_SERVICE("frequent errors while using the service", 6),

    // 해당사항 없음
    NONE("none", 7);

    private String desc;
    private long code;

    private static final Map<Object, WithdrawalGroundsType> map = EnumUtils.getMap(WithdrawalGroundsType.class);
    public static final WithdrawalGroundsType getType(Object value) {
        return map.get(value);
    }
}
