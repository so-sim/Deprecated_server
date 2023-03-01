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

        Group groupEntity = groupRepository.save(group);
        CreatedGroupDto createdGroupDto = CreatedGroupDto.builder().groupId(groupEntity.getId()).build();

        return createdGroupDto;
    }
}
