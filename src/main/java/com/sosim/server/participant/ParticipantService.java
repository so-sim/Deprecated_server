package com.sosim.server.participant;

import com.sosim.server.config.exception.CustomException;
import com.sosim.server.group.Group;
import com.sosim.server.participant.dto.request.ParticipantNicknameRequest;
import com.sosim.server.type.CodeType;
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
        if (participantRepository.findByUserAndGroup(userEntity, groupEntity).isPresent()) {
            throw new CustomException(CodeType.ALREADY_INTO_GROUP);
        }

        if (participantRepository.findByNicknameAndGroup(nickname, groupEntity).isPresent()) {
            throw new CustomException(CodeType.ALREADY_USE_NICKNAME);
        }

        Participant participant = Participant.create(userEntity, groupEntity, nickname);
        saveParticipantEntity(participant);
    }

    public void deleteParticipantEntity(User user, Group group) {
        participantRepository.delete(getParticipantEntity(user, group));
    }

    public void modifyNickname(User user, Group group, ParticipantNicknameRequest participantNicknameRequest) {
        Participant participantEntity = getParticipantEntity(user, group);
        participantEntity.modifyNickname(participantNicknameRequest.getNickname());
    }

    public void saveParticipantEntity(Participant participant) {
        participantRepository.save(participant);
    }

    public Participant getParticipantEntity(String nickname, Group group) {
        return participantRepository.findByNicknameAndGroup(nickname, group)
                .orElseThrow(() -> new CustomException(CodeType.NONE_PARTICIPANT));
    }

    public Participant getParticipantEntity(User user, Group group) {
        return participantRepository.findByUserAndGroup(user, group)
                .orElseThrow(() -> new CustomException(CodeType.NONE_PARTICIPANT));
    }

    public Slice<Participant> getParticipantSlice(Long index, Long userId) {
        if (index == 0) {
            return participantRepository.findByIdGreaterThanAndUserIdOrderByIdDesc(index, userId,
                    PageRequest.ofSize(17));
        }
        return participantRepository.findByIdLessThanAndUserIdOrderByIdDesc(index, userId,
                PageRequest.ofSize(18));
    }
}
