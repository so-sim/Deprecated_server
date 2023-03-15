package com.sosim.server.participant;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ParticipantType {
    @JsonProperty("group_admin")
    ADMIN,
    @JsonProperty("group_member")
    MEMBER
}
