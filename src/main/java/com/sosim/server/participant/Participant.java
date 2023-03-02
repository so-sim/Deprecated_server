package com.sosim.server.participant;

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
public class Participant {
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

    @Column(name = "PARTICIPANT_NAME")
    private String participantName;

    @Column(name = "PARTICIPANT_TYPE")
    @Enumerated(EnumType.STRING)
    private ParticipantType participantType;

    @Column(name = "JOIN_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime joinDate;

    @Column(name = "WITHDRAW_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime withdrawDate;

    @Builder
    private Participant(User user, Group group, String participantName, ParticipantType participantType) {
        this.user = user;
        this.group = group;
        this.participantName = participantName;
        this.participantType = participantType;
        this.joinDate = LocalDateTime.now();
    }

    public static Participant createParticipant(User user, Group group, String participantName, ParticipantType participantType) {
        return Participant.builder()
                .user(user)
                .group(group)
                .participantName(participantName)
                .participantType(participantType)
                .build();
    }
}
