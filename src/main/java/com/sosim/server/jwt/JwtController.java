package com.sosim.server.jwt;

import static com.sosim.server.common.constant.MessageConstant.REISSUETOKEN;

import com.sosim.server.common.response.Response;
import com.sosim.server.jwt.dto.ReIssueTokenInfo;
import com.sosim.server.jwt.dto.ReIssueTokenReq;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class JwtController {

    private final JwtService jwtService;

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
