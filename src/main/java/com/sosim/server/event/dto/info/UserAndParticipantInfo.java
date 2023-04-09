package com.sosim.server.event.dto.info;

import com.sosim.server.participant.Participant;
import com.sosim.server.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAndParticipantInfo {
    User user;
    Participant participant;
}
