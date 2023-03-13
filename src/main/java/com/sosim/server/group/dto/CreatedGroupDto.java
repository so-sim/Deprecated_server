package com.sosim.server.group.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sosim.server.group.Group;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CreatedGroupDto {
    @JsonProperty("group_id")
    private Long groupId;

    public static CreatedGroupDto create(Group group) {
        return CreatedGroupDto.builder()
                .groupId(group.getId())
                .build();
    }
}