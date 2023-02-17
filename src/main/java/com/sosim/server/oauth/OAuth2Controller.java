package com.sosim.server.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;

    @GetMapping("/login/oauth2/code/{provider}")
    public ResponseEntity<?> login(@PathVariable("provider") String provider, @RequestParam("code") String code) {
        oAuth2Service.login(Provider.getProvider(provider), code);
        return null;
    }
}
