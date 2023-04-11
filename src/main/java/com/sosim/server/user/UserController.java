package com.sosim.server.user;

import com.sosim.server.common.response.Response;
import com.sosim.server.security.AuthUser;
import com.sosim.server.type.CodeType;
import com.sosim.server.user.dto.req.UserWithdrawalReq;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @DeleteMapping("/api/user/withdraw")
    public ResponseEntity<?> withdrawalUser(@Valid @RequestBody UserWithdrawalReq userWithdrawalReq) {
        this.userService.withdrawalUser(userWithdrawalReq);
        return new ResponseEntity<>(Response.create(CodeType.USER_WITHDRAWAL_SUCCESS, null), CodeType.USER_WITHDRAWAL_SUCCESS.getHttpStatus());
    }

    @GetMapping("/api/user/withdraw")
    public ResponseEntity<?> withdrawInfo(@AuthenticationPrincipal AuthUser authUser) {
        userService.withdrawInfo(Long.parseLong(authUser.getId()));
        return new ResponseEntity<>(Response.create(CodeType.CAN_WITHDRAWAL, null), CodeType.CAN_WITHDRAWAL.getHttpStatus());
    }
}
