package com.sosim.server.user;

import com.sosim.server.type.SocialType;
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

    private String email;
    private String password;

    private LocalDate createDate;

    private LocalDate withdrawalDate;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    // TODO long으로 넘어오는지 String으로 넘어오는지 확인
    private String socialId;

    // TODO role에 관해 논의 필요
//    @Enumerated(EnumType.STRING)
//    private Role role;

//    public void authorizeUser() {
//        this.role = Role.USER;
//    }

//    public void passwordEncode(PasswordEncoder passwordEncoder) {
//        this.password = passwordEncoder.encode(this.password);
//    }

}
