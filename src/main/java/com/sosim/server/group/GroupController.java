package com.sosim.server.group;

import com.sosim.server.common.response.Response;
import com.sosim.server.group.dto.CreateGroupDto;
import com.sosim.server.group.dto.CreatedGroupDto;
import com.sosim.server.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/group")
    public ResponseEntity<?> createGroup(@Validated @RequestBody CreateGroupDto createGroupDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return getResponseEntityFromBindingException(bindingResult);
        }

        CreatedGroupDto createdGroupDto = groupService.createGroup(createGroupDto);
        Response<?> response = Response.createResponse("모임이 성공적으로 생성되었습니다.", createdGroupDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getResponseEntityFromBindingException(BindingResult bindingResult){
        return new ResponseEntity<>(Response.createResponse("잘못된 요청 형식입니다.", null), HttpStatus.BAD_REQUEST);
    }
}
