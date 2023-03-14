package com.sosim.server.group;

import com.sosim.server.common.response.Response;
import com.sosim.server.config.exception.CustomException;
import com.sosim.server.group.dto.request.CreateGroupRequest;
import com.sosim.server.group.dto.response.CreateGroupResponse;
import com.sosim.server.group.dto.response.GetGroupListResponse;
import com.sosim.server.group.dto.response.GetGroupResponse;
import com.sosim.server.group.dto.request.UpdateGroupRequest;
import com.sosim.server.participant.dto.response.GetParticipantListResponse;
import com.sosim.server.participant.dto.request.ParticipantNicknameRequest;
import com.sosim.server.security.AuthUser;
import com.sosim.server.type.CodeType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/group")
    public ResponseEntity<?> createGroup(@AuthenticationPrincipal AuthUser authUser,
                                         @Validated @RequestBody CreateGroupRequest createGroupRequest,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingError(bindingResult);
        }

        CreateGroupResponse createGroupResponse = groupService.createGroup(Long.valueOf(authUser.getId()), createGroupRequest);
        Response<?> response = Response.create(CodeType.CREATE_GROUP, createGroupResponse);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<?> getGroup(@PathVariable("groupId") Long groupId) {
        GetGroupResponse getGroupResponse = groupService.getGroup(groupId);
        Response<?> response = Response.create(CodeType.GET_GROUP, getGroupResponse);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/group/{groupId}/participants")
    public ResponseEntity<?> getGroupParticipants(@PathVariable("groupId") Long groupId) {
        GetParticipantListResponse groupParticipant = groupService.getGroupParticipant(groupId);
        Response<?> response = Response.create(CodeType.GET_PARTICIPANTS, groupParticipant);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/group/{groupId}")
    public ResponseEntity<?> modifyGroup(@AuthenticationPrincipal AuthUser authUser,
                                         @PathVariable("groupId") Long groupId,
                                         @Validated @RequestBody UpdateGroupRequest updateGroupRequest,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingError(bindingResult);
        }

        CreateGroupResponse updatedGroupDto = groupService.updateGroup(Long.valueOf(authUser.getId()), groupId, updateGroupRequest);
        Response<?> response = Response.create(CodeType.MODIFY_GROUP, updatedGroupDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/group/{groupId}")
    public ResponseEntity<?> deleteGroup(@AuthenticationPrincipal AuthUser authUser,
                                         @PathVariable("groupId") Long groupId) {
        groupService.deleteGroup(Long.parseLong(authUser.getId()), groupId);
        Response<?> response = Response.create(CodeType.DELETE_GROUP, null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/group/{groupId}/participant")
    public ResponseEntity<?> intoGroup(@AuthenticationPrincipal AuthUser authUser,
                                       @PathVariable("groupId") Long groupId,
                                       @Validated @RequestBody ParticipantNicknameRequest participantNicknameRequest,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingError(bindingResult);
        }

        groupService.intoGroup(Long.parseLong(authUser.getId()), groupId, participantNicknameRequest);
        Response<?> response = Response.create(CodeType.INTO_GROUP, null);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/group/{groupId}/admin")
    public ResponseEntity<?> modifyAdmin(@AuthenticationPrincipal AuthUser authUser,
                                         @PathVariable("groupId") Long groupId,
                                         @RequestBody ParticipantNicknameRequest participantNicknameRequest) {

        groupService.modifyAdmin(Long.parseLong(authUser.getId()), groupId, participantNicknameRequest);
        Response<?> response = Response.create(CodeType.MODIFY_GROUP_ADMIN, null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/group/{groupId}/participant")
    public ResponseEntity<?> withdrawGroup(@AuthenticationPrincipal AuthUser authUser,
                                           @PathVariable("groupId") Long groupId) {
        groupService.withdrawGroup(Long.parseLong(authUser.getId()), groupId);
        Response<?> response = Response.create(CodeType.WITHDRAW_GROUP, null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/group/{groupId}/participant")
    public ResponseEntity<?> modifyNickname(@AuthenticationPrincipal AuthUser authUser,
                                            @PathVariable ("groupId") Long groupId,
                                            @Validated @RequestBody ParticipantNicknameRequest participantNicknameRequest) {
        groupService.modifyNickname(Long.parseLong(authUser.getId()), groupId, participantNicknameRequest);
        Response<?> response = Response.create(CodeType.MODIFY_NICKNAME, null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/groups")
    public ResponseEntity<?> getMyGroups(@AuthenticationPrincipal AuthUser authUser,
                                         @RequestParam("index") Long index) {
        GetGroupListResponse groupList = groupService.getMyGroups(index, Long.parseLong(authUser.getId()));
        Response<?> response = Response.create(CodeType.GET_GROUPS, groupList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void bindingError(BindingResult bindingResult) {
        throw new CustomException(CodeType.BINDING_ERROR, bindingResult.getFieldError().getField(),
                bindingResult.getFieldError().getDefaultMessage());
    }
}
