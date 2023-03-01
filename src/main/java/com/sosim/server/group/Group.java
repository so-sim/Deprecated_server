package com.sosim.server.group;

import com.sosim.server.participant.Participant;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GROUP_ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "ADMIN_ID")
    private Long adminId;

    @Column(name = "ADMIN_NICKNAME")
    private String adminNickname;

    @Column(name = "CREATE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createDate;

    @Column(name = "UPDATE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updateDate;

    @Column(name = "DELETE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deleteDate;

    @Column(name = "COVER_COLOR_TYPE")
    @Enumerated(EnumType.STRING)
    private CoverColorType coverColorType;

    @Column(name = "GROUP_TYPE")
    @Enumerated(EnumType.STRING)
    private GroupType groupType;

    @OneToMany(mappedBy = "group")
    private List<Participant> participantList;

    @Builder(access = AccessLevel.PRIVATE)
    private Group(Long adminId, String title, String coverColorType, String groupType) {
        this.adminId = adminId;
        this.title = title;
        this.coverColorType = CoverColorType.valueOf(coverColorType);
        this.groupType = GroupType.of(groupType);
        createDate = LocalDateTime.now();
        updateDate = LocalDateTime.now();
    }

    public static Group createGroup(Long adminId, String title, String groupType, String coverColorType) {
        return Group.builder()
                .adminId(adminId)
                .title(title)
                .groupType(groupType)
                .coverColorType(coverColorType).build();
    }
}
