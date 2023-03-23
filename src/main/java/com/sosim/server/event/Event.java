package com.sosim.server.event;

import com.sosim.server.common.auditing.BaseTimeEntity;
import com.sosim.server.event.dto.req.EventModifyReq;
import com.sosim.server.event.dto.req.PaymentTypeReq;
import com.sosim.server.group.Group;
import com.sosim.server.type.EventType;
import com.sosim.server.type.PaymentType;
import com.sosim.server.type.StatusType;
import com.sosim.server.user.User;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@Getter
@NoArgsConstructor
@Entity
@Builder
@Table(name = "EVENT")
@AllArgsConstructor
public class Event extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @NotNull
    @Column(name = "GROUNDS_DATE")
    private LocalDateTime groundsDate;

    @NotNull
    @Column(name = "PAYMENT")
    private Long payment;

    @NotEmpty
    @Size(max=65)
    @Column(name = "GROUNDS")
    private String grounds;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_TYPE")
    private PaymentType paymentType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_TYPE")
    private StatusType statusType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "EVENT_TYPE")
    private EventType eventType;

    public void updateEvent(EventModifyReq eventModifyReq) {

        if (eventModifyReq.getUserName() != null && eventModifyReq.getUser() != null) {
            this.user = eventModifyReq.getUser();
        }

        if (eventModifyReq.getGroundsDate() != null) {
            this.groundsDate = eventModifyReq.getGroundsDate();
        }

        if (!ObjectUtils.isEmpty(eventModifyReq.getPayment())) {
            this.payment = eventModifyReq.getPayment();
        }

        if (eventModifyReq.getGrounds() != null) {
            this.grounds = eventModifyReq.getGrounds();
        }

        if (eventModifyReq.getPaymentType() != null) {
            this.paymentType = PaymentType.getType(eventModifyReq.getPaymentType());
        }
    }

    public void deleteEvent(){
        this.statusType = StatusType.DELETED;
    }

    public void changePaymentType(PaymentTypeReq paymentTypeReq) {
        this.paymentType = PaymentType.getType(paymentTypeReq.getPaymentType());
    }
}
