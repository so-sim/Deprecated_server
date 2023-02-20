package com.sosim.server.user;

import com.sosim.server.jwt.JwtFactory;
import com.sosim.server.jwt.JwtService;
import com.sosim.server.jwt.util.property.JwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

//oauth2?
//    @GetMapping("/user")
//    public String getUser(@AuthenticationPrincipal AuthUser authUser){
//        //userServer.getUser(String userId);
//    }

}
