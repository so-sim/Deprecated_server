package com.sosim.server.event;

import com.sosim.server.group.Group;
import com.sosim.server.type.PaymentType;
import com.sosim.server.type.StatusType;
import com.sosim.server.user.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByPaymentTypeAndStatusTypeAndGroup(PaymentType paymentType, StatusType statusType, Group group);

    Page<Event> findByGroupAndUserAndStatusType(Group group, User user, StatusType statusType, Pageable pageable);
    Optional<Event> findByPaymentTypeAndStatusTypeAndCreateDateBetween(PaymentType paymentType, StatusType statusType, LocalDateTime startDate, LocalDateTime endDate);

    Optional<Event> findByIdAndStatusType(Long id, StatusType statusType);
}
