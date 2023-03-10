package com.sosim.server.group.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sosim.server.group.Group;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GetGroupDto {
    @JsonProperty("title")
    private String title;

    @JsonProperty("created_date")
    private LocalDateTime createDate;

    @JsonProperty("update_date")
    private LocalDateTime updateDate;

    @JsonProperty("cover_color")
    private String coverColorType;

    @JsonProperty("group_type")
    private String groupType;

    public static GetGroupDto create(Group group) {
        return GetGroupDto.builder()
                .title(group.getTitle())
                .createDate(group.getCreateDate())
                .updateDate(group.getUpdateDate())
                .coverColorType(group.getCoverColorType().getLabel())
                .groupType(group.getGroupType().getLabel())
                .build();
    }
}
