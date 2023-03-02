package com.sosim.server.group;

import com.sosim.server.group.dto.CreateGroupDto;
import com.sosim.server.group.dto.CreatedGroupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    public CreatedGroupDto createGroup(Long adminId, CreateGroupDto createGroupDto) {
        if (groupRepository.findByTitle(createGroupDto.getTitle()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이름의 모임입니다.");
        }

        Group group = Group.createGroup(adminId, createGroupDto.getTitle(), createGroupDto.getGroupType(),
                createGroupDto.getCoverColorType());

        Group groupEntity = saveGroupEntity(group);
        CreatedGroupDto createdGroupDto = CreatedGroupDto.builder().groupId(groupEntity.getId()).build();

        return createdGroupDto;
    }

    public Group getGroupEntity(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모임을 찾을 수 없습니다."));
    }

    public Group saveGroupEntity(Group group) {
        return groupRepository.save(group);
    }
}
