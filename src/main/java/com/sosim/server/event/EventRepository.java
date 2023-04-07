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
    Page<Event> findByGroupAndPaymentTypeAndStatusType(Group group, PaymentType paymentType, StatusType statusType, Pageable pageable);
    Long countByGroupAndPaymentTypeAndStatusType(Group group, PaymentType paymentType, StatusType statusType);
    Page<Event> findByGroupAndStatusTypeAndGroundsDateBetween(Group group, StatusType statusType, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Page<Event> findByGroupAndPaymentTypeAndStatusTypeAndGroundsDateBetween(Group group, PaymentType paymentType, StatusType statusType, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    List<Event> findByGroupAndStatusTypeAndGroundsDateBetween(Group group, StatusType statusType, LocalDateTime startDate, LocalDateTime endDate);
    Long countByGroupAndStatusTypeAndGroundsDateBetween(Group group, StatusType statusType, LocalDateTime startDate, LocalDateTime endDate);
    Long countByGroupAndUserAndStatusType(Group group, User user, StatusType statusType);
    Page<Event> findByGroupAndStatusType(Group group, StatusType statusType, Pageable pageable);
    Long countByGroupAndStatusType(Group group, StatusType statusType);
    Optional<Event> findByIdAndStatusType(Long id, StatusType statusType);
}
