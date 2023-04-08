package com.sosim.server.participant.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sosim.server.group.Group;
import com.sosim.server.participant.Participant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetParticipantListResponse {
    @JsonProperty("adminId")
    private Long adminId;

    @JsonProperty("adminNickname")
    private String adminNickname;

    @JsonProperty("nicknameList")
    private List<Member> memberList;

    public static GetParticipantListResponse create(Group group, List<Member> memberList) {
        return GetParticipantListResponse.builder()
                .adminId(group.getAdminId())
                .adminNickname(group.getAdminNickname())
                .memberList(memberList)
                .build();
    }

    @Getter
    @AllArgsConstructor
    public static class Member {
        private Long userId;
        private String nickname;
    }
}
