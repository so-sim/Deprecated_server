package com.sosim.server.participant.dto.response;

import com.sosim.server.participant.Participant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class GetNicknameResponse {

    private String nickname;

    public static GetNicknameResponse create(Participant participant) {
        return GetNicknameResponse.builder().nickname(participant.getNickname()).build();
    }
}
