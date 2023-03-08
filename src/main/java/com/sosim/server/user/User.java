package com.sosim.server.user;

import com.sosim.server.type.SocialType;
import com.sosim.server.type.UserType;
import com.sosim.server.type.WithdrawalGroundsType;
import java.time.LocalDateTime;
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

@Getter
@NoArgsConstructor
@Entity
@Builder
@Table(name = "USER")
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @NotNull
    private LocalDateTime createDate;

    private LocalDateTime withdrawalDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    //TODO 구글이랑 네이버 long으로 넘어오는지 확인
    @NotNull
    private String socialId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Enumerated(EnumType.ORDINAL)
    private WithdrawalGroundsType withdrawalGroundsType;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWithdrawalDate(LocalDateTime withdrawalDate) {
        this.withdrawalDate = withdrawalDate;
    }

    public void setWithdrawalGroundsType(WithdrawalGroundsType withdrawalGroundsType) {
        this.withdrawalGroundsType = withdrawalGroundsType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
