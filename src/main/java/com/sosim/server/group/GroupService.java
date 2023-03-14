package com.sosim.server.group;

import com.sosim.server.config.exception.CustomException;
import com.sosim.server.group.dto.request.CreateGroupRequest;
import com.sosim.server.group.dto.request.UpdateGroupRequest;
import com.sosim.server.group.dto.response.CreateGroupResponse;
import com.sosim.server.group.dto.response.GetGroupResponse;
import com.sosim.server.group.dto.response.GetGroupListResponse;
import com.sosim.server.participant.Participant;
import com.sosim.server.participant.ParticipantService;
import com.sosim.server.participant.dto.GetParticipantsDto;
import com.sosim.server.participant.dto.ParticipantNicknameDto;
import com.sosim.server.type.ErrorCodeType;
import com.sosim.server.user.User;
import com.sosim.server.user.UserRepository;
import com.sosim.server.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;
    private final ParticipantService participantService;
    private final UserService userService;
    private final UserRepository userRepository;

    public CreateGroupResponse createGroup(Long userId, CreateGroupRequest createGroupRequest) {
        User userEntity = userService.getUser(userId);
        Group group = Group.create(userId, createGroupRequest);
        Group groupEntity = saveGroupEntity(group);

        participantService.createParticipant(userEntity, groupEntity, createGroupRequest.getNickname());

        CreateGroupResponse createGroupResponse = CreateGroupResponse.create(groupEntity);
        return createGroupResponse;
    }

    public GetGroupResponse getGroup(Long groupId) {
        Group groupEntity = getGroupEntity(groupId);
        GetGroupResponse getGroupResponse = GetGroupResponse.create(groupEntity);

        return getGroupResponse;
    }

    public GetParticipantsDto getGroupParticipant(Long groupId) {
        Group groupEntity = getGroupEntity(groupId);
        List<Participant> participantList = groupEntity.getParticipantList();
        GetParticipantsDto getList = GetParticipantsDto.create(groupEntity, participantList);

        return getList;
    }

    public CreateGroupResponse updateGroup(Long userId, Long groupId, UpdateGroupRequest updateGroupRequest) {
        Group groupEntity = getGroupEntity(groupId);

        if (!groupEntity.getAdminId().equals(userId)) {
            throw new CustomException(ErrorCodeType.NONE_ADMIN);
        }
        groupEntity.update(updateGroupRequest);
        CreateGroupResponse updateGroup = CreateGroupResponse.create(groupEntity);

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
        User userEntity = userService.getUser(userId);
        Group groupEntity = getGroupEntity(groupId);

        participantService.createParticipant(userEntity, groupEntity, participantNicknameDto.getNickname());
    }

    public void modifyAdmin(Long userId, Long groupId, ParticipantNicknameDto participantNicknameDto) {
        Group groupEntity = getGroupEntity(groupId);

        if (!groupEntity.getAdminId().equals(userId)) {
            throw new CustomException(ErrorCodeType.NONE_ADMIN);
        }

        Participant participantEntity = participantService
                .getParticipantEntity(participantNicknameDto.getNickname(), groupEntity);

        if (!groupEntity.getParticipantList().contains(participantEntity)) {
            throw new CustomException(ErrorCodeType.NONE_PARTICIPANT);
        }

        groupEntity.modifyAdmin(participantEntity);
    }

    public void withdrawGroup(Long userId, Long groupId) {
        participantService.deleteParticipantEntity(userService.getUser(userId), getGroupEntity(groupId));
    }

    public void modifyNickname(Long userId, Long groupId, ParticipantNicknameDto participantNicknameDto) {
        participantService.modifyNickname(userService.getUser(userId),
                getGroupEntity(groupId), participantNicknameDto);
    }

    public GetGroupListResponse getMyGroups(Long index, Long userId) {
        Slice<Participant> slice = participantService.getParticipantSlice(index, userId);
        List<Participant> participantEntityList = slice.getContent();

        if (participantEntityList.size() == 0) {
            throw new CustomException(ErrorCodeType.NO_MORE_GROUP);
        }

        List<GetGroupResponse> groupList = new ArrayList<>();
        for (Participant participant : participantEntityList) {
            groupList.add(GetGroupResponse.create(participant.getGroup()));
        }

        return GetGroupListResponse.create(participantEntityList.get(participantEntityList.size() - 1).getId(),
                slice.hasNext(), groupList);
    }

    public Group getGroupEntity(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCodeType.NOT_FOUND_GROUP));
    }

    public Group saveGroupEntity(Group group) {
        return groupRepository.save(group);
    }
}
