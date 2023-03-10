package com.sosim.server.group;

import com.sosim.server.common.auditing.BaseTimeEntity;
import com.sosim.server.group.dto.CreateGroupDto;
import com.sosim.server.group.dto.UpdateGroupDto;
import com.sosim.server.participant.Participant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Participant> participantList;

    @Builder(access = AccessLevel.PRIVATE)
    private Group(String title, Long adminId, String adminNickname,
                  String coverColorType, String groupType) {
        this.title = title;
        this.adminId = adminId;
        this.adminNickname = adminNickname;
        this.coverColorType = CoverColorType.of(coverColorType);
        this.groupType = GroupType.of(groupType);
    }

    public static Group create(Long adminId, CreateGroupDto createGroupDto) {
        return Group.builder()
                .title(createGroupDto.getTitle())
                .adminId(adminId)
                .adminNickname(createGroupDto.getNickname())
                .groupType(createGroupDto.getGroupType())
                .coverColorType(createGroupDto.getCoverColorType())
                .build();
    }

    public void update(UpdateGroupDto updateGroupDto) {
        this.title = updateGroupDto.getTitle();
        this.groupType = GroupType.of(updateGroupDto.getGroupType());
        this.coverColorType = CoverColorType.of(updateGroupDto.getCoverColorType());
    }

    public void modifyAdmin(Participant participant) {
        adminId = participant.getUser().getId();
        adminNickname = participant.getNickname();
    }
}
