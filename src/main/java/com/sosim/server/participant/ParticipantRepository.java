package com.sosim.server.participant;

import com.sosim.server.group.Group;
import com.sosim.server.type.StatusType;
import com.sosim.server.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findByNicknameAndGroupAndStatus(String participantName, Group group, StatusType statusType);

    Optional<Participant> findByUserAndGroupAndStatus(User user, Group group, StatusType statusType);

    Optional<Participant> findByNickname(String participantName);

    Optional<Participant> findByUser(User user);

    Slice<Participant> findByIdAndStatusGreaterThanAndUserIdOrderByIdDesc(Long participantId, StatusType statusType, Long userId, Pageable pageable);

    Slice<Participant> findByIdAndStatusLessThanAndUserIdOrderByIdDesc(Long participantId, StatusType statusType, Long userId, Pageable pageable);
}
