package com.sosim.server.group;

import com.sosim.server.common.response.Response;
import com.sosim.server.config.exception.CustomException;
import com.sosim.server.group.dto.request.CreateGroupRequest;
import com.sosim.server.group.dto.response.CreateGroupResponse;
import com.sosim.server.group.dto.response.GetGroupResponse;
import com.sosim.server.group.dto.request.UpdateGroupRequest;
import com.sosim.server.participant.dto.GetParticipantsDto;
import com.sosim.server.participant.dto.ParticipantNicknameDto;
import com.sosim.server.security.AuthUser;
import com.sosim.server.type.ErrorCodeType;
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
        Response<?> response = Response.createResponse("모임이 성공적으로 생성되었습니다.", createGroupResponse);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<?> getGroup(@PathVariable("groupId") Long groupId) {
        GetGroupResponse getGroupResponse = groupService.getGroup(groupId);
        Response<?> response = Response.createResponse("모임이 성공적으로 조회되었습니다.", getGroupResponse);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/group/{groupId}/participants")
    public ResponseEntity<?> getGroupParticipant(@PathVariable("groupId") Long groupId) {
        GetParticipantsDto groupParticipant = groupService.getGroupParticipant(groupId);
        Response<?> response = Response.createResponse("모임 참가자가 성공적으로 조회되었습니다.", groupParticipant);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/group/{groupId}")
    public ResponseEntity<?> updateGroup(@AuthenticationPrincipal AuthUser authUser,
                                         @PathVariable("groupId") Long groupId,
                                         @Validated @RequestBody UpdateGroupRequest updateGroupRequest,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingError(bindingResult);
        }

        CreateGroupResponse updatedGroupDto = groupService.updateGroup(Long.valueOf(authUser.getId()), groupId, updateGroupRequest);
        Response<?> response = Response.createResponse("모임이 성공적으로 수정되었습니다.", updatedGroupDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/group/{groupId}")
    public ResponseEntity<?> deleteGroup(@AuthenticationPrincipal AuthUser authUser,
                                         @PathVariable("groupId") Long groupId) {
        groupService.deleteGroup(Long.parseLong(authUser.getId()), groupId);
        Response<?> response = Response.createResponse("모임이 성공적으로 삭제되었습니다.", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/group/{groupId}/participant")
    public ResponseEntity<?> intoGroup(@AuthenticationPrincipal AuthUser authUser,
                                       @PathVariable("groupId") Long groupId,
                                       @Validated @RequestBody ParticipantNicknameDto participantNicknameDto,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingError(bindingResult);
        }

        groupService.intoGroup(Long.parseLong(authUser.getId()), groupId, participantNicknameDto);
        Response<?> response = Response.createResponse("모임에 성공적으로 참가되었습니다.", null);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/group/{groupId}/admin")
    public ResponseEntity<?> modifyAdmin(@AuthenticationPrincipal AuthUser authUser,
                                         @PathVariable("groupId") Long groupId,
                                         @RequestBody ParticipantNicknameDto participantNicknameDto) {

        groupService.modifyAdmin(Long.parseLong(authUser.getId()), groupId, participantNicknameDto);
        Response<?> response = Response.createResponse("관리자가 성공적으로 변경되었습니다.", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/group/{groupId}/participant")
    public ResponseEntity<?> withdrawGroup(@AuthenticationPrincipal AuthUser authUser,
                                           @PathVariable("groupId") Long groupId) {
        groupService.withdrawGroup(Long.parseLong(authUser.getId()), groupId);
        Response<?> response = Response.createResponse("성공적으로 모임에서 탈퇴되었습니다.", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/group/{groupId}/participant")
    public ResponseEntity<?> modifyNickname(@AuthenticationPrincipal AuthUser authUser,
                                            @PathVariable ("groupId") Long groupId,
                                            @Validated @RequestBody ParticipantNicknameDto participantNicknameDto) {
        groupService.modifyNickname(Long.parseLong(authUser.getId()), groupId, participantNicknameDto);
        Response<?> response = Response.createResponse("성공적으로 닉네임이 수정되었습니다.", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/groups")
    public ResponseEntity<?> getMyGroups(@AuthenticationPrincipal AuthUser authUser,
                                         @RequestParam("index") Long index) {
        return new ResponseEntity<>(groupService.getMyGroups(index, Long.parseLong(authUser.getId())), HttpStatus.OK);
    }

    private void bindingError(BindingResult bindingResult) {
        throw new CustomException(ErrorCodeType.BINDING_ERROR, bindingResult.getFieldError().getField(),
                bindingResult.getFieldError().getDefaultMessage());
    }
}
