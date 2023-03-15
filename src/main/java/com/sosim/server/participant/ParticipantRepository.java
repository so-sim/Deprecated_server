package com.sosim.server.participant;

import com.sosim.server.participant.dto.GetParticipantDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    @Query(value = "select new com.sosim.server.participant.dto.GetParticipantDto" +
            "(p.participantName, p.participantType)" +
            "from Participant p where p.group.id = :groupId")
    List<GetParticipantDto> findAllByGroup(@Param("groupId") Long groupId);

    boolean existsByParticipantNameAndGroupId(String participantName, Long groupId);
}
