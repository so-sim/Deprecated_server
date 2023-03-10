package com.sosim.server.group;

import com.sosim.server.common.util.EnumValidAble;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum CoverColorType implements EnumValidAble {
    RED("#f86565"),
    ORANGE("#f89a65"),
    YELLOW("#f8e065"),
    BLUE("#658ef8"),
    PURPLE("#9465f8");

    private final String label;

    CoverColorType(String label) {
        this.label = label;
    }

    private static final Map<String, CoverColorType> map =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(CoverColorType::getLabel, Function.identity())));

    public static CoverColorType of(String label) {
        return map.get(label);
    }
}
