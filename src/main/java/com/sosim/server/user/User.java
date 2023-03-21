package com.sosim.server.user;

import com.sosim.server.common.auditing.BaseTimeEntity;
import com.sosim.server.type.SocialType;
import com.sosim.server.type.UserType;
import com.sosim.server.type.WithdrawalGroundsType;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Builder
@Table(name = "USER")
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Setter
    @Column(name = "EMAIL")
    private String email;

    @Setter
    @Column(name = "WITHDRAWAL_DATE")
    private LocalDateTime withdrawalDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "SOCIAL_TYPE")
    private SocialType socialType;

    //TODO 구글이랑 네이버 long으로 넘어오는지 확인
    @NotNull
    @Column(name = "SOCIAL_ID")
    private String socialId;

    @Setter
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "USER_TYPE")
    private UserType userType;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "WITHDRAWAL_GROUNDS_TYPE")
    private WithdrawalGroundsType withdrawalGroundsType;

}
