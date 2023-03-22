package com.sosim.server.participant;

import com.sosim.server.group.Group;
import com.sosim.server.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    @Query("select p from Participant p where p.nickname = :nickname and p.group = :group and status = using")
    Optional<Participant> findByNicknameAndGroup(@Param("nickname") String participantName, @Param("group") Group group);

    @Query("select p from Participant p where p.user = :user and p.group = :group and status = using")
    Optional<Participant> findByUserAndGroup(@Param("user") User user, @Param("group") Group group);

    Optional<Participant> findByNickname(String participantName);

    Optional<Participant> findByUser(User user);

    @Query("select p from Participant p where p.id = :id and status = using " +
            "and p.user.id > :userId order by p.id desc")
    Slice<Participant> findByIdGreaterThanAndUserIdOrderByIdDesc(@Param("id")Long participantId, @Param("userId")Long userId, Pageable pageable);

    @Query("select p from Participant p where p.id = :id and status = using " +
            "and p.user.id < :userId order by p.id desc")
    Slice<Participant> findByIdLessThanAndUserIdOrderByIdDesc(@Param("id") Long participantId, @Param("userId") Long userId, Pageable pageable);
}
