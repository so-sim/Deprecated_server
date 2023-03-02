package com.sosim.server.group.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sosim.server.group.Group;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreatedUpdatedGroupDto {
    @JsonProperty("group_id")
    private Long groupId;

    public static CreatedUpdatedGroupDto createCreatedUpdatedGroupDto(Group group) {
        return CreatedUpdatedGroupDto.builder()
                .groupId(group.getId())
                .build();
    }
}