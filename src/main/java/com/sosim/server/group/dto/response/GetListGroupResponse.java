package com.sosim.server.group.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class GetListGroupResponse {
    @JsonProperty("index")
    private Long index;

    @JsonProperty("next")
    private boolean next;

    @JsonProperty("group_list")
    List<GetGroupResponse> groupList;

    public static GetListGroupResponse create(Long index, boolean next, List<GetGroupResponse> groupList) {
        return GetListGroupResponse.builder()
                .index(index)
                .next(next)
                .groupList(groupList)
                .build();
    }
}
