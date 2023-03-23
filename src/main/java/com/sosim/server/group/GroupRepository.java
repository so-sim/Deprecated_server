package com.sosim.server.group;

import com.sosim.server.type.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByIdAndStatusType(Long groupId, StatusType statusType);
}
