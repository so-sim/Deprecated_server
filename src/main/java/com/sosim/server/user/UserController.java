package com.sosim.server.user;

import com.sosim.server.common.response.Response;
import com.sosim.server.type.CodeType;
import com.sosim.server.user.dto.req.UserWithdrawalReq;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PutMapping("/api/user")
    public ResponseEntity<?> withdrawalUser(@Valid @RequestBody UserWithdrawalReq userWithdrawalReq) {
        this.userService.withdrawalUser(userWithdrawalReq);
        return new ResponseEntity<>(Response.create(CodeType.USER_WITHDRAWAL_SUCCESS, null), CodeType.USER_WITHDRAWAL_SUCCESS.getHttpStatus());
    }
}
