package com.sosim.server.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sosim.server.config.exception.CustomException;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum CoverColorType {
    RED("#f86565"),
    ORANGE("#f89a65"),
    YELLOW("#f8e065"),
    BLUE("#658ef8"),
    PURPLE("#9465f8");

    private final String code;

    CoverColorType(String code) {
        this.code = code;
    }

    private static final Map<String, CoverColorType> map =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(CoverColorType::getCode, Function.identity())));

    public static CoverColorType of(String label) {
        return map.get(label);
    }

    @JsonCreator
    public static CoverColorType create(String label){
        for(CoverColorType coverColorType : CoverColorType.values()){
            if(coverColorType.getCode().equals(label)){
                return coverColorType;
            }
        }
        throw new CustomException(CodeType.BINDING_ERROR);
    }
}
