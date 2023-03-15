package com.sosim.server.participant;

import com.sosim.server.group.Group;
import com.sosim.server.group.GroupService;
import com.sosim.server.participant.dto.GetParticipantDto;
import com.sosim.server.participant.dto.ParticipantNicknameDto;
import com.sosim.server.user.User;
import com.sosim.server.user.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final GroupService groupService;

    public void createParticipant(Long userId, Long groupId, ParticipantNicknameDto participantNicknameDto) {
        if (participantRepository.existsByParticipantNameAndGroupId(participantNicknameDto.getNickname(), groupId)) {
            throw new IllegalArgumentException("이미 모임에서 사용중인 닉네임입니다.");
        }

        // UserService 로 refactoring
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
        Group group = groupService.getGroupEntity(groupId);

        Participant participant = Participant.createParticipant(user, group, participantNicknameDto.getNickname(),
                ParticipantType.valueOf(participantNicknameDto.getParticipantType().toUpperCase()));
        Participant participantEntity = saveParticipantEntity(participant);

        // 모임 ADMIN 설정
        if (participantEntity.getParticipantType().equals(ParticipantType.ADMIN)) {
            groupService.setGroupAdmin(groupId, participantEntity);
        }
    }

    public List<GetParticipantDto> getParticipantList(Long groupId) {
        return participantRepository.findAllByGroup(groupId);
    }

    public Participant getParticipantEntity(Long participantId) {
        return participantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("해당 참가자를 찾을 수 없습니다."));
    }

    public Participant saveParticipantEntity(Participant participant) {
        return participantRepository.save(participant);
    }
}
