package com.sosim.server.participant;

import com.sosim.server.config.exception.CustomException;
import com.sosim.server.group.Group;
import com.sosim.server.participant.dto.request.ParticipantNicknameRequest;
import com.sosim.server.participant.dto.response.GetNicknameResponse;
import com.sosim.server.type.CodeType;
import com.sosim.server.type.StatusType;
import com.sosim.server.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    public void createParticipant(User userEntity, Group groupEntity, String nickname) {
        if (participantRepository.findByUserAndGroupAndStatusType(userEntity, groupEntity, StatusType.ACTIVE).isPresent()) {
            throw new CustomException(CodeType.ALREADY_INTO_GROUP);
        }

        if (participantRepository.findByNicknameAndGroupAndStatusType(nickname, groupEntity, StatusType.ACTIVE).isPresent()) {
            throw new CustomException(CodeType.ALREADY_USE_NICKNAME);
        }

        Participant participant = Participant.create(userEntity, groupEntity, nickname);
        saveParticipantEntity(participant);
    }

    public void deleteParticipantEntity(User user, Group group) {
        getParticipantEntity(user, group).delete();
    }

    public void modifyNickname(User user, Group group, ParticipantNicknameRequest participantNicknameRequest) {
        Participant participantEntity = getParticipantEntity(user, group);
        participantEntity.modifyNickname(participantNicknameRequest.getNickname());
    }

    public void saveParticipantEntity(Participant participant) {
        participantRepository.save(participant);
    }

    public Participant getParticipantEntity(String nickname, Group group) {
        return participantRepository.findByNicknameAndGroupAndStatusType(nickname, group, StatusType.ACTIVE)
                .orElseThrow(() -> new CustomException(CodeType.NONE_PARTICIPANT));
    }

    public Participant getParticipantEntity(User user, Group group) {
        return participantRepository.findByUserAndGroupAndStatusType(user, group, StatusType.ACTIVE)
                .orElseThrow(() -> new CustomException(CodeType.NONE_PARTICIPANT));
    }

    public Slice<Participant> getParticipantSlice(Long index, Long userId) {
        if (index == 0) {
            return participantRepository.findByUserIdAndStatusTypeOrderByIdDesc(userId, StatusType.ACTIVE,
                    PageRequest.ofSize(17));
        }
        return participantRepository.findByIdLessThanAndStatusTypeAndUserIdOrderByIdDesc(index, StatusType.ACTIVE, userId,
                PageRequest.ofSize(18));
    }

    public GetNicknameResponse getMyNickname(User user, Group group) {
        return GetNicknameResponse.create(getParticipantEntity(user, group));
    }

    public Long getCountParticipantAtGroup(Long groupId) {
        return participantRepository.countByGroupIdAndStatusType(groupId, StatusType.ACTIVE);
    }
}
