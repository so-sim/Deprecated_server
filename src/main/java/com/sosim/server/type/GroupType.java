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
public enum GroupType {
    STUDY("스터디"),
    CAMPUS("학교, 교내/외 모임"),
    COMPANY("회사, 사내 모임"),
    HOBBY("취미, 동호회 모임"),
    SOCIAL("친구, 사모임"),
    PROJECT("프로젝트"),
    ETC("기타");

    private final String label;

    GroupType(String label) {
        this.label = label;
    }

    private static final Map<String, GroupType> map =
        Collections.unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(GroupType::getLabel, Function.identity())));

    public static GroupType of(String label) {
        return map.get(label);
    }

    @JsonCreator
    public static GroupType create(String label){
        for(GroupType groupType : GroupType.values()){
            if(groupType.getLabel().equals(label)){
                return groupType;
            }
        }
        throw new CustomException(CodeType.BINDING_ERROR);
    }
}
