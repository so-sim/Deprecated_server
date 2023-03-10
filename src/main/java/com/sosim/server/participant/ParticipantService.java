package com.sosim.server.participant;

import com.sosim.server.config.exception.CustomException;
import com.sosim.server.group.Group;
import com.sosim.server.type.ErrorCodeType;
import com.sosim.server.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    public void createParticipant(User userEntity, Group groupEntity, String nickname) {
        if (participantRepository.findByNicknameAndGroupId(nickname, groupEntity.getId()).isPresent()) {
            throw new IllegalArgumentException("이미 모임에 존재하는 닉네임입니다.");
        }

        Participant participant = Participant.create(userEntity, groupEntity, nickname);
        saveParticipantEntity(participant);
    }

    public Participant saveParticipantEntity(Participant participant) {
        return participantRepository.save(participant);
    }

    public Participant getParticipantEntity(String nickname, Group group) {
        return participantRepository.findByNicknameAndGroupId(nickname, group.getId())
                .orElseThrow(() -> new CustomException(ErrorCodeType.NONE_PARTICIPANT));
    }
}
