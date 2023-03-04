package com.sosim.server.user;

import static com.sosim.server.jwt.util.constant.MessageConstant.REISSUETOKEN;
import static com.sosim.server.jwt.util.constant.MessageConstant.USER_INFO_SUCCESS;

import com.sosim.server.common.response.Response;
import com.sosim.server.jwt.JwtFactory;
import com.sosim.server.jwt.JwtService;
import com.sosim.server.jwt.dto.ReIssueTokenInfo;
import com.sosim.server.jwt.dto.ReIssueTokenReq;
import com.sosim.server.jwt.util.property.JwtProperties;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;
    private final JwtProperties jwtProperties;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtFactory jwtFactory;
    private final JwtService jwtService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") long id) {
        User user = this.userService.getUser(id);
        Response<?> response = Response.builder().message(USER_INFO_SUCCESS).content(user).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login/reissueToken")
    public ResponseEntity<?> reIssueToken(@RequestBody ReIssueTokenReq reIssueTokenReq, HttpServletResponse httpServletResponse) {

        ReIssueTokenInfo reIssueTokenInfo = this.jwtService.verifyRefreshTokenAndReIssueAccessToken(reIssueTokenReq, httpServletResponse);
        if (reIssueTokenInfo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        Response<?> response = Response.builder().message(REISSUETOKEN).content(reIssueTokenInfo).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
