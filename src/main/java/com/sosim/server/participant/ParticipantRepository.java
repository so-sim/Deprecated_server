package com.sosim.server.participant;

import com.sosim.server.group.Group;
import com.sosim.server.type.StatusType;
import com.sosim.server.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findByNicknameAndGroupAndStatusType(String participantName, Group group, StatusType statusType);

    Optional<Participant> findByUserAndGroupAndStatusType(User user, Group group, StatusType statusType);

    Optional<Participant> findByNickname(String participantName);

    Slice<Participant> findByUserIdAndStatusTypeOrderByIdDesc(Long userId, StatusType statusType, Pageable pageable);

    Slice<Participant> findByIdLessThanAndStatusTypeAndUserIdOrderByIdDesc(Long participantId, StatusType statusType, Long userId, Pageable pageable);

    @Query("select p.user.id from Participant p where p.group.id = :groupId and p.statusType = 'ACTIVE' and p.nickname in (:nickname)")
    List<Long> findByGroupIdAndNicknameIn(@Param("groupId") Long groupId, @Param("nickname") List<String> nickname);
}
