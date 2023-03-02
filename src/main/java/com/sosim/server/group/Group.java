package com.sosim.server.group;

import com.sosim.server.common.auditing.BaseTimeEntity;
import com.sosim.server.group.dto.CreateUpdateGroupDto;
import com.sosim.server.group.dto.CreatedUpdatedGroupDto;
import com.sosim.server.participant.Participant;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Group extends BaseTimeEntity {
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

    @Column(name = "DELETE_DATE")
    private LocalDateTime deleteDate;

    @Column(name = "COVER_COLOR_TYPE")
    @Enumerated(EnumType.STRING)
    private CoverColorType coverColorType;

    @Column(name = "GROUP_TYPE")
    @Enumerated(EnumType.STRING)
    private GroupType groupType;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<Participant> participantList;

    @Builder(access = AccessLevel.PRIVATE)
    private Group(String title, String coverColorType, String groupType) {
        this.title = title;
        this.coverColorType = CoverColorType.valueOf(coverColorType);
        this.groupType = GroupType.of(groupType);
    }

    public static Group createGroup(CreateUpdateGroupDto createUpdateGroupDto) {
        return Group.builder()
                .title(createUpdateGroupDto.getTitle())
                .groupType(createUpdateGroupDto.getGroupType())
                .coverColorType(createUpdateGroupDto.getCoverColorType())
                .build();
    }

    public void setAdmin(Participant participant) {
        adminId = participant.getId();
        adminNickname = participant.getParticipantName();
    }

    public void update(CreateUpdateGroupDto createUpdateGroupDto) {
        title = createUpdateGroupDto.getTitle();
        coverColorType = CoverColorType.valueOf(createUpdateGroupDto.getCoverColorType());
        groupType = GroupType.of(createUpdateGroupDto.getGroupType());
    }
}