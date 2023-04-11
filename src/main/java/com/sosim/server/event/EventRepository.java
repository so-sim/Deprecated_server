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

    Optional<Event> findByIdAndStatusType(Long id, StatusType statusType);
    List<Event> findByGroupAndStatusTypeAndGroundsDateBetween(Group group, StatusType statusType, LocalDateTime startDate, LocalDateTime endDate);
    Page<Event> findByGroupAndStatusType(Group group, StatusType statusType, Pageable pageable);
    List<Event> findListByGroupAndUserAndStatusType(Group group, User user, StatusType statusType);
    Page<Event> findByGroupAndPaymentTypeAndStatusType(Group group, PaymentType paymentType, StatusType statusType, Pageable pageable);
    List<Event> findListByGroupAndUserAndPaymentTypeAndStatusType(Group group, User user, PaymentType paymentType, StatusType statusType);
    Page<Event> findByGroupAndStatusTypeAndGroundsDateBetween(Group group, StatusType statusType, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    List<Event> findListByGroupAndUserAndStatusTypeAndGroundsDateBetween(Group group, User user, StatusType statusType, LocalDateTime startDate, LocalDateTime endDate);
    Page<Event> findByGroupAndPaymentTypeAndStatusTypeAndGroundsDateBetween(Group group, PaymentType paymentType, StatusType statusType, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    List<Event> findListByGroupAndUserAndPaymentTypeAndStatusTypeAndGroundsDateBetween(Group group, User user, PaymentType paymentType, StatusType statusType, LocalDateTime startDate, LocalDateTime endDate);
}
