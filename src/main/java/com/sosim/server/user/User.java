package com.sosim.server.user;

import com.sosim.server.type.Provider;
import com.sosim.server.type.UserType;
import com.sosim.server.type.WithdrawalGroundsType;
import java.time.LocalDateTime;
import javax.persistence.Entity;
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
@Table(name = "USERS")
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO nullable
    private String email;

    //TODO password 어떻게 할건지
    private String password;

    @NotNull
    private LocalDateTime createDate;

    // nullable
    private LocalDateTime withdrawalDate;

    // 넘겨줄땐 String으로 줄것
    private Provider socialType;

    // TODO long으로 넘어오는지 String으로 넘어오는지 확인
    @NotNull
    private String socialId;

    // TODO Type이 mysql에 어떻게 들어가는지 확인->요상하면 String으로 바꾸기
    @NotNull
    private UserType userType;

    // nullable
    private WithdrawalGroundsType withdrawalGroundsType;

    // TODO role에 관해 논의 필요
//    @Enumerated(EnumType.STRING)
//    private Role role;

//    public void authorizeUser() {
//        this.role = Role.USER;
//    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWithdrawalDate(LocalDateTime withdrawalDate) {
        this.withdrawalDate = withdrawalDate;
    }

    public void setWithdrawalGroundsType(WithdrawalGroundsType withdrawalGroundsType) {
        this.withdrawalGroundsType = withdrawalGroundsType;
    }
}
