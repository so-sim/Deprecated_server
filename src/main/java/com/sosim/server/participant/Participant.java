package com.sosim.server.participant;

import com.sosim.server.common.auditing.BaseTimeEntity;
import com.sosim.server.group.Group;
import com.sosim.server.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Participant extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PARTICIPANT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID")
    private Group group;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "WITHDRAW_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime withdrawDate;

    @Builder
    private Participant(User user, Group group, String nickname) {
        this.user = user;
        this.group = group;
        this.nickname = nickname;
    }

    public static Participant create(User user, Group group, String nickname) {
        return Participant.builder()
                .user(user)
                .group(group)
                .nickname(nickname)
                .build();
    }
}
