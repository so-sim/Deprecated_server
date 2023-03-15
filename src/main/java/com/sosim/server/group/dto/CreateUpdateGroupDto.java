package com.sosim.server.group.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateUpdateGroupDto {
    @NotBlank
    @Size(min = 1, max = 15, message = "모임 이름은 최소 1글자, 최대 15글자까지 허용됩니다.")
    private String title;

    @NotNull
    private String groupType;

    @NotNull
    private String coverColorType;
}
