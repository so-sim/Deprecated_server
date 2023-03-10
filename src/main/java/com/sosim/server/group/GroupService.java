package com.sosim.server.group;

import com.sosim.server.config.exception.CustomException;
import com.sosim.server.group.dto.CreateGroupDto;
import com.sosim.server.group.dto.CreatedGroupDto;
import com.sosim.server.group.dto.GetGroupDto;
import com.sosim.server.group.dto.UpdateGroupDto;
import com.sosim.server.participant.Participant;
import com.sosim.server.participant.ParticipantService;
import com.sosim.server.participant.dto.GetParticipantsDto;
import com.sosim.server.participant.dto.ParticipantNicknameDto;
import com.sosim.server.type.ErrorCodeType;
import com.sosim.server.user.User;
import com.sosim.server.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;
    private final ParticipantService participantService;
    private final UserService userService;

    public CreatedGroupDto createGroup(Long userId, CreateGroupDto createGroupDto) {
        User userEntity = userService.getUserEntity(userId);
        Group group = Group.create(userId, createGroupDto);
        Group groupEntity = saveGroupEntity(group);

        participantService.createParticipant(userEntity, groupEntity, createGroupDto.getNickname());

        CreatedGroupDto createdGroupDto = CreatedGroupDto.create(groupEntity);
        return createdGroupDto;
    }

    public GetGroupDto getGroup(Long groupId) {
        Group groupEntity = getGroupEntity(groupId);
        GetGroupDto getGroupDto = GetGroupDto.create(groupEntity);

        return getGroupDto;
    }

    public GetParticipantsDto getGroupParticipant(Long groupId) {
        Group groupEntity = getGroupEntity(groupId);
        List<Participant> participantList = groupEntity.getParticipantList();
        GetParticipantsDto getList = GetParticipantsDto.create(groupEntity, participantList);

        return getList;
    }

    public CreatedGroupDto updateGroup(Long userId, Long groupId, UpdateGroupDto updateGroupDto) {
        Group groupEntity = getGroupEntity(groupId);

        if (!groupEntity.getAdminId().equals(userId)) {
            throw new CustomException(ErrorCodeType.NONE_ADMIN);
        }
        groupEntity.update(updateGroupDto);
        CreatedGroupDto updateGroup = CreatedGroupDto.create(groupEntity);

        return updateGroup;
    }

    public void deleteGroup(Long userId, Long groupId) {
        Group groupEntity = getGroupEntity(groupId);

        if (!groupEntity.getAdminId().equals(userId)) {
            throw new CustomException(ErrorCodeType.NONE_ADMIN);
        }

        groupRepository.delete(groupEntity);
    }

    public void intoGroup(Long userId, Long groupId, ParticipantNicknameDto participantNicknameDto) {
        User userEntity = userService.getUserEntity(userId);
        Group groupEntity = getGroupEntity(groupId);

        participantService.createParticipant(userEntity, groupEntity, participantNicknameDto.getNickname());
    }

    public void modifyAdmin(Long userId, Long groupId, ParticipantNicknameDto participantNicknameDto) {
        Group groupEntity = getGroupEntity(groupId);

        if (!groupEntity.getAdminId().equals(userId)) {
            throw new CustomException(ErrorCodeType.NONE_ADMIN);
        }

        Participant participantEntity = participantService.getParticipantEntity(participantNicknameDto.getNickname(), groupEntity);

        if (!groupEntity.getParticipantList().contains(participantEntity)) {
            throw new CustomException(ErrorCodeType.NONE_PARTICIPANT);
        }

        groupEntity.modifyAdmin(participantEntity);
    }

    public Group getGroupEntity(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCodeType.NOT_FOUND_GROUP));
    }

    public Group saveGroupEntity(Group group) {
        return groupRepository.save(group);
    }
}
