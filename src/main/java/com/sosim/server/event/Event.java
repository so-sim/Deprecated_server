package com.sosim.server.event;

import com.sosim.server.common.auditing.BaseTimeEntity;
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

@Getter
@NoArgsConstructor
@Entity
@Builder
@Table(name = "EVENT")
@AllArgsConstructor
public class Event extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "GROUNDS_DATE")
    @NotNull
    private LocalDateTime groundsDate;

    @Column(name = "DELETED_DATE")
    private LocalDateTime deletedDate;

    @NotNull
    private Long payment;

    @NotEmpty
    @Size(max=65)
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
}
