package com.sosim.server.group;

import com.sosim.server.group.dto.CreateUpdateGroupDto;
import com.sosim.server.group.dto.CreatedUpdatedGroupDto;
import com.sosim.server.group.dto.GetGroupDto;
import com.sosim.server.participant.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;

    public CreatedUpdatedGroupDto createGroup(CreateUpdateGroupDto createUpdateGroupDto) {
        if (groupRepository.existsByTitle(createUpdateGroupDto.getTitle())) {
            throw new IllegalArgumentException("이미 존재하는 이름의 모임입니다.");
        }

        Group group = Group.createGroup(createUpdateGroupDto);
        Group groupEntity = saveGroupEntity(group);
        CreatedUpdatedGroupDto createdUpdatedGroupDto = CreatedUpdatedGroupDto.createCreatedUpdatedGroupDto(groupEntity);

        return createdUpdatedGroupDto;
    }

    public GetGroupDto getGroup(Long groupId) {
        Group groupEntity = getGroupEntity(groupId);
        GetGroupDto getGroupDto = GetGroupDto.createGetGroupDto(groupEntity);

        return getGroupDto;
    }

    public CreatedUpdatedGroupDto updateGroup(Long groupId, CreateUpdateGroupDto createUpdateGroupDto) {
        Group groupEntity = getGroupEntity(groupId);
        groupEntity.update(createUpdateGroupDto);
        CreatedUpdatedGroupDto createdUpdatedGroupDto = CreatedUpdatedGroupDto.createCreatedUpdatedGroupDto(groupEntity);

        return createdUpdatedGroupDto;
    }

    public Group getGroupEntity(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모임을 찾을 수 없습니다."));
    }

    public Group saveGroupEntity(Group group) {
        return groupRepository.save(group);
    }

    public void setGroupAdmin(Long groupId, Participant participant) {
        Group groupEntity = getGroupEntity(groupId);
        groupEntity.setAdmin(participant);
    }
}
