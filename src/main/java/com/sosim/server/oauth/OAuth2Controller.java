package com.sosim.server.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sosim.server.common.response.Response;
import com.sosim.server.oauth.dto.OAuth2JwtResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;

    @GetMapping("/login/oauth2/code/{socialType}")
    public ResponseEntity<?> login(@PathVariable("socialType") String socialType, @RequestParam("code") String code,
                                   HttpServletResponse response) throws JsonProcessingException {
        OAuth2JwtResponseDto tokens = oAuth2Service.login(SocialType.getProvider(socialType), code);

        // Cookie RefreshToken 설정 - HttpOnly
        ResponseCookie cookie = ResponseCookie.from("refreshToken", tokens.getRefreshToken().getRefreshToken())
                        .maxAge(7 * 24 * 60 * 60).secure(true)
                        .sameSite("None").httpOnly(true).build();
        response.setHeader("Set-Cookie", cookie.toString());

        // Response 생성
        Response<?> responseDto = Response.createResponse("로그인이 성공적으로 완료되었습니다.", tokens);

        return ResponseEntity.ok(responseDto);
    }
}
