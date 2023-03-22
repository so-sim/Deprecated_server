package com.sosim.server.type;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ParticipantType {
    @JsonProperty("group_admin")
    ADMIN,
    @JsonProperty("group_member")
    MEMBER
}
