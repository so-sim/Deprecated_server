package com.sosim.server.group.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sosim.server.group.CoverColorType;
import com.sosim.server.group.Group;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetGroupDto {
    private String title;

    @JsonProperty("admin_nickname")
    private String adminNickname;

    @JsonProperty("created_date")
    private LocalDateTime createdDate;

    @JsonProperty("modified_date")
    private LocalDateTime modifiedDate;

    @JsonProperty("cover_color")
    private CoverColorType coverColorType;

    @JsonProperty("group_type")
    private String groupType;

    public static GetGroupDto createGetGroupDto(Group group) {
        return GetGroupDto.builder()
                .title(group.getTitle())
                .adminNickname(group.getAdminNickname())
                .createdDate(group.getCreatedDate())
                .modifiedDate(group.getModifiedDate())
                .coverColorType(group.getCoverColorType())
                .groupType(group.getGroupType().getLabel())
                .build();
    }
}
