package com.sosim.server.group.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sosim.server.group.CoverColorType;
import com.sosim.server.group.Group;
import com.sosim.server.participant.Participant;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class GetGroupDto {
    private String title;

    @JsonProperty("admin_nickname")
    private String adminNickname;

    @JsonProperty("create_date")
    private LocalDateTime createDate;

    @JsonProperty("update_date")
    private LocalDateTime updateDate;

    @JsonProperty("cover_color")
    private CoverColorType coverColorType;

    @JsonProperty("group_type")
    private String groupType;

    public static GetGroupDto createGetGroupDto(Group group) {
        return GetGroupDto.builder()
                .title(group.getTitle())
                .adminNickname(group.getAdminNickname())
                .createDate(group.getCreateDate())
                .updateDate(group.getModifiedDate())
                .coverColorType(group.getCoverColorType())
                .groupType(group.getGroupType().getLabel())
                .build();
    }
}
