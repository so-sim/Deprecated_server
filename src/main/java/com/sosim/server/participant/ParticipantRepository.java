package com.sosim.server.participant;

import com.sosim.server.group.Group;
import com.sosim.server.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findByNicknameAndGroup(String participantName, Group group);
    Optional<Participant> findByUserAndGroup(User user, Group group);
}
