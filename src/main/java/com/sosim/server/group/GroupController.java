package com.sosim.server.group;

import com.sosim.server.common.response.Response;
import com.sosim.server.group.dto.CreateUpdateGroupDto;
import com.sosim.server.group.dto.CreatedUpdatedGroupDto;
import com.sosim.server.group.dto.GetGroupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/group")
    public ResponseEntity<?> createGroup(@Validated @RequestBody CreateUpdateGroupDto createUpdateGroupDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return getResponseEntityFromBindingException(bindingResult);
        }

        CreatedUpdatedGroupDto createdUpdatedGroupDto = groupService.createGroup(createUpdateGroupDto);
        Response<?> response = Response.createResponse("모임이 성공적으로 생성되었습니다.", createdUpdatedGroupDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<?> getGroup(@PathVariable Long groupId) {
        GetGroupDto getGroupDto = groupService.getGroup(groupId);
        Response<?> response = Response.createResponse("모임이 성공적으로 조회되었습니다.", getGroupDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/group/{groupId}")
    public ResponseEntity<?> updateGroup(@PathVariable Long groupId,
                                      @Validated @RequestBody CreateUpdateGroupDto createUpdateGroupDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return getResponseEntityFromBindingException(bindingResult);
        }

        CreatedUpdatedGroupDto createdUpdatedGroupDto = groupService.updateGroup(groupId, createUpdateGroupDto);
        Response<?> response = Response.createResponse("모임이 성공적으로 수정되었습니다.", createdUpdatedGroupDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> getResponseEntityFromBindingException(BindingResult bindingResult){
        return new ResponseEntity<>(Response.createResponse("잘못된 요청 형식입니다.", null), HttpStatus.BAD_REQUEST);
    }
}
