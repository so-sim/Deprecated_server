package com.sosim.server.participant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sosim.server.participant.ParticipantType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetParticipantDto {
    @JsonProperty("nickname")
    private String participantName;

    @JsonProperty("participant_type")
    private ParticipantType participantType;
}
