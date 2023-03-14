package com.sosim.server.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sosim.server.common.response.Response;
import com.sosim.server.jwt.JwtService;
import com.sosim.server.oauth.dto.response.LoginResponse;
import com.sosim.server.type.CodeType;
import com.sosim.server.type.SocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;
    private final JwtService jwtService;

    @PostMapping("/login/oauth2/code/{socialType}")
    public ResponseEntity<?> login(@PathVariable("socialType") String socialType, @RequestParam("code") String code,
                                   HttpServletResponse response) throws JsonProcessingException {
        LoginResponse tokens = oAuth2Service.login(SocialType.getSocialType(socialType), code);

        // Cookie RefreshToken 설정
        jwtService.setRefreshTokenHeader(response, tokens.getRefreshToken().getRefreshToken());

        // Response 생성
        Response<?> responseDto = Response.create(CodeType.SUCCESS_LOGIN, tokens);

        return ResponseEntity.ok(responseDto);
    }
}
