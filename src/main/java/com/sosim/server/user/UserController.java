package com.sosim.server.user;

import static com.sosim.server.jwt.util.constant.MessageConstant.REISSUETOKEN;

import com.sosim.server.common.response.Response;
import com.sosim.server.jwt.JwtFactory;
import com.sosim.server.jwt.JwtService;
import com.sosim.server.jwt.dto.ReIssueTokenInfo;
import com.sosim.server.jwt.dto.ReIssueTokenReq;
import com.sosim.server.jwt.util.property.JwtProperties;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(
    onConstructor = @__(@Autowired)
)
@Slf4j
public class UserController {
    // private final UserService userService;
    private JwtProperties jwtProperties;
    private RedisTemplate<String, String> redisTemplate;
    private JwtFactory jwtFactory;
    private JwtService jwtService;
    @PostMapping("/login/reissueToken")
    public ResponseEntity<?> reIssueToken(@RequestBody ReIssueTokenReq reIssueTokenReq, HttpServletResponse response) {

        ReIssueTokenInfo reIssueTokenInfo = this.jwtService.verifyRefreshTokenAndReIssueAccessToken(reIssueTokenReq, response);
        if (reIssueTokenInfo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        Response<?> result = Response.builder().message(REISSUETOKEN).content(reIssueTokenInfo).build();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
