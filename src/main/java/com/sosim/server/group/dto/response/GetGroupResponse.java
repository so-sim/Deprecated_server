package com.sosim.server.group.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sosim.server.group.Group;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class GetGroupResponse {
    @JsonProperty("group_id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("cover_color")
    private String coverColorType;

    @JsonProperty("group_type")
    private String groupType;

    @JsonProperty("admin_nickname")
    private String adminNickname;

    public static GetGroupResponse create(Group group) {
        return GetGroupResponse.builder()
                .id(group.getId())
                .title(group.getTitle())
                .coverColorType(group.getCoverColorType().getCode())
                .groupType(group.getGroupType().getLabel())
                .adminNickname(group.getAdminNickname())
                .build();
    }
}
