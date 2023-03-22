package com.sosim.server.group.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sosim.server.type.CoverColorType;
import com.sosim.server.type.GroupType;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class CreateGroupRequest {
    @NotBlank
    @Size(min = 1, max = 15, message = "모임 이름은 최소 1글자, 최대 15글자까지 허용됩니다.")
    @JsonProperty("groupName")
    private String title;

    @NotBlank
    @Size(min = 1, max = 15, message = "닉네임은 최소 1글자, 최대 15글자까지 허용됩니다.")
    @JsonProperty("myName")
    private String nickname;

    @JsonProperty("type")
    private GroupType groupType;

    @JsonProperty("coverColor")
    private CoverColorType coverColorType;
}
