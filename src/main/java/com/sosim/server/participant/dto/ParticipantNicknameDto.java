package com.sosim.server.participant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ParticipantNicknameDto {

    @NotBlank
    @Size(min = 1, max = 15, message = "닉네임은 최소 1글자, 최대 15글자까지 허용됩니다.")
    private String nickname;

    @NotBlank
    @JsonProperty("participant_type")
    private String participantType;
}
