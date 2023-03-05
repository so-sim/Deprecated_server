package com.sosim.server.participant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    //SELECT PARTICIPANT_NAME FROM PARTICIPANT WHERE GROUP_ID = 1
    @Query(value = "select p.participantName from Participant p where p.group.id = :groupId")
    List<String> getGroupParticipantNickname(@Param("groupId") Long groupId);

    Optional<Participant> findByUserIdAndGroupId(Long userId, Long groupId);
}
