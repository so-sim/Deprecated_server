package com.sosim.server.group.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreatedGroupDto {
    @JsonProperty("group_id")
    private Long groupId;

}