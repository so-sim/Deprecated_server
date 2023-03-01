package com.sosim.server.group;

import com.sosim.server.group.dto.CreateGroupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    public void createGroup(Long adminId, CreateGroupDto createGroupDto) {
        Optional<Group> entityGroup = groupRepository.findByTitle(createGroupDto.getTitle());
        if (entityGroup.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이름의 모임입니다.");
        }

        Group newGroup = Group.createGroup(adminId, createGroupDto.getTitle(), createGroupDto.getGroupType(),
                createGroupDto.getCoverColorType());
        groupRepository.save(newGroup);
    }
}
