package com.sosim.server.user;

import com.sosim.server.common.response.Response;
import com.sosim.server.type.CodeType;
import com.sosim.server.user.dto.req.UserWithdrawalReq;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") long id) {
        User user = this.userService.getUser(id);
        return new ResponseEntity<>(Response.create(CodeType.USER_INFO_SUCCESS, user), CodeType.USER_INFO_SUCCESS.getHttpStatus());
    }

    @PutMapping
    public ResponseEntity<?> withdrawalUser(@Valid @RequestBody UserWithdrawalReq userWithdrawalReq) {
        this.userService.withdrawalUser(userWithdrawalReq);
        return new ResponseEntity<>(Response.create(CodeType.USER_WITHDRAWAL_SUCCESS, null), CodeType.USER_WITHDRAWAL_SUCCESS.getHttpStatus());
    }

}
