package com.sosim.server.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    @Override
    @Query("select g from Group g where g.id = :groupId and g.status = using")
    Optional<Group> findById(@Param("groupId") Long groupId);
}
