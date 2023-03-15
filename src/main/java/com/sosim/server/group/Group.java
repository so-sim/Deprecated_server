package com.sosim.server.group;

import com.sosim.server.common.auditing.BaseTimeEntity;
import com.sosim.server.group.dto.CreateUpdateGroupDto;
import com.sosim.server.participant.Participant;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "GROUPS")
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
        this.coverColorType = CoverColorType.of(coverColorType);
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
        coverColorType = CoverColorType.of(createUpdateGroupDto.getCoverColorType());
        groupType = GroupType.of(createUpdateGroupDto.getGroupType());
    }
}
