package com.sosim.server.group;

import com.sosim.server.config.exception.CustomException;
import com.sosim.server.group.dto.request.CreateGroupRequest;
import com.sosim.server.group.dto.request.UpdateGroupRequest;
import com.sosim.server.group.dto.response.CreateGroupResponse;
import com.sosim.server.group.dto.response.GetGroupResponse;
import com.sosim.server.group.dto.response.GetGroupListResponse;
import com.sosim.server.participant.Participant;
import com.sosim.server.participant.ParticipantService;
import com.sosim.server.participant.dto.response.GetNicknameResponse;
import com.sosim.server.participant.dto.response.GetParticipantListResponse;
import com.sosim.server.participant.dto.request.ParticipantNicknameRequest;
import com.sosim.server.type.CodeType;
import com.sosim.server.user.User;
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

    public CreateGroupResponse createGroup(Long userId, CreateGroupRequest createGroupRequest) {
        User userEntity = userService.getUser(userId);
        Group groupEntity = saveGroupEntity(Group.create(userId, createGroupRequest));

        participantService.createParticipant(userEntity, groupEntity, createGroupRequest.getNickname());

        return CreateGroupResponse.create(groupEntity);
    }

    public GetGroupResponse getGroup(Long userId, Long groupId) {
        Group groupEntity = getGroupEntity(groupId);

        return GetGroupResponse.create(groupEntity, groupEntity.getAdminId().equals(userId));
    }

    public GetParticipantListResponse getGroupParticipant(Long groupId) {
        Group groupEntity = getGroupEntity(groupId);
        List<Participant> participantList = groupEntity.getParticipantList();

        return GetParticipantListResponse.create(groupEntity, participantList);
    }

    public CreateGroupResponse updateGroup(Long userId, Long groupId, UpdateGroupRequest updateGroupRequest) {
        Group groupEntity = getGroupEntity(groupId);

        if (!groupEntity.getAdminId().equals(userId)) {
            throw new CustomException(CodeType.NONE_ADMIN);
        }
        groupEntity.update(updateGroupRequest);

        return CreateGroupResponse.create(groupEntity);
    }

    public void deleteGroup(Long userId, Long groupId) {
        Group groupEntity = getGroupEntity(groupId);

        if (!groupEntity.getAdminId().equals(userId)) {
            throw new CustomException(CodeType.NONE_ADMIN);
        }

        groupEntity.delete();
    }

    public void intoGroup(Long userId, Long groupId, ParticipantNicknameRequest participantNicknameRequest) {
        User userEntity = userService.getUser(userId);
        Group groupEntity = getGroupEntity(groupId);

        participantService.createParticipant(userEntity, groupEntity, participantNicknameRequest.getNickname());
    }

    public void modifyAdmin(Long userId, Long groupId, ParticipantNicknameRequest participantNicknameRequest) {
        Group groupEntity = getGroupEntity(groupId);

        if (!groupEntity.getAdminId().equals(userId)) {
            throw new CustomException(CodeType.NONE_ADMIN);
        }

        Participant participantEntity = participantService
                .getParticipantEntity(participantNicknameRequest.getNickname(), groupEntity);

        if (!groupEntity.getParticipantList().contains(participantEntity)) {
            throw new CustomException(CodeType.NONE_PARTICIPANT);
        }

        groupEntity.modifyAdmin(participantEntity);
    }

    public void withdrawGroup(Long userId, Long groupId) {
        participantService.deleteParticipantEntity(userService.getUser(userId), getGroupEntity(groupId));
    }

    public void modifyNickname(Long userId, Long groupId, ParticipantNicknameRequest participantNicknameRequest) {
        participantService.modifyNickname(userService.getUser(userId),
                getGroupEntity(groupId), participantNicknameRequest);
    }

    public GetGroupListResponse getMyGroups(Long index, Long userId) {
        Slice<Participant> slice = participantService.getParticipantSlice(index, userId);
        List<Participant> participantEntityList = slice.getContent();

        if (participantEntityList.size() == 0) {
            throw new CustomException(CodeType.NO_MORE_GROUP);
        }

        List<GetGroupResponse> groupList = new ArrayList<>();
        for (Participant participant : participantEntityList) {
            Group group = participant.getGroup();
            groupList.add(GetGroupResponse.create(group, group.getAdminId().equals(userId)));
        }

        return GetGroupListResponse.create(participantEntityList.get(participantEntityList.size() - 1).getId(),
                slice.hasNext(), groupList);
    }

    public GetNicknameResponse getMyNickname(Long userId, Long groupId) {
        return participantService.getMyNickname(userService.getUser(userId), getGroupEntity(groupId));
    }

    public Group getGroupEntity(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_GROUP));
    }

    public Group saveGroupEntity(Group group) {
        return groupRepository.save(group);
    }
}
