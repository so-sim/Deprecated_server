package com.sosim.server.group.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ListGroupDto {
    @JsonProperty("index")
    private Long index;

    @JsonProperty("next")
    private boolean next;

    @JsonProperty("group_list")
    List<GetGroupDto> groupList;

    public static ListGroupDto create(Long index, boolean next, List<GetGroupDto> groupList) {
        return ListGroupDto.builder()
                .index(index)
                .next(next)
                .groupList(groupList)
                .build();
    }
}
