package com.sosim.server.user;

import com.sosim.server.type.SocialType;
import com.sosim.server.type.UserType;
import com.sosim.server.type.WithdrawalGroundsType;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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

    // TODO password 어떻게 할건지
    private String password;

    private LocalDate createDate;

    private LocalDate withdrawalDate;

    // 넘겨줄땐 String으로 줄것
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    // TODO long으로 넘어오는지 String으로 넘어오는지 확인
    private String socialId;

    private UserType userType;

    private WithdrawalGroundsType withdrawalGroundsType;

    // TODO role에 관해 논의 필요
//    @Enumerated(EnumType.STRING)
//    private Role role;

//    public void authorizeUser() {
//        this.role = Role.USER;
//    }

}
