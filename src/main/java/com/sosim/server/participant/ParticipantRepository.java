package com.sosim.server.participant;

import com.sosim.server.group.Group;
import com.sosim.server.type.StatusType;
import com.sosim.server.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findByNicknameAndGroupAndStatusType(String participantName, Group group, StatusType statusType);

    Optional<Participant> findByUserAndGroupAndStatusType(User user, Group group, StatusType statusType);

    Optional<Participant> findByNickname(String participantName);

    Optional<Participant> findByUser(User user);

    Slice<Participant> findByUserIdAndStatusTypeOrderByIdDesc(Long userId, StatusType statusType, Pageable pageable);

    Long countByGroupIdAndStatusType(Long groupId, StatusType statusType);
}
