package com.sosim.server.participant;

import com.sosim.server.common.response.Response;
import com.sosim.server.participant.dto.GetParticipantDto;
import com.sosim.server.participant.dto.ParticipantNicknameDto;
import com.sosim.server.security.AuthUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping("participant/{groupId}")
    public ResponseEntity<?> setNickname(
            @AuthenticationPrincipal AuthUser authUser, @PathVariable("groupId") Long groupId,
            @Validated @RequestBody ParticipantNicknameDto participantNicknameDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return getResponseEntityFromBindingException(bindingResult);
        }

        participantService.createParticipant(Long.valueOf(authUser.getId()), groupId, participantNicknameDto);
        Response<?> response = Response.createResponse("참가자가 정상적으로 설정되었습니다.", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/group/{groupId}/participant")
    public ResponseEntity<?> getGroupParticipant(@PathVariable Long groupId) {
        List<GetParticipantDto> participantList = participantService.getParticipantList(groupId);
        Response<?> response = Response.createResponse("모임 참가자 리스트가 정상적으로 조회되었습니다.", participantList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> getResponseEntityFromBindingException(BindingResult bindingResult){
        return new ResponseEntity<>(Response.createResponse("잘못된 요청 형식입니다.", null), HttpStatus.BAD_REQUEST);
    }
}
