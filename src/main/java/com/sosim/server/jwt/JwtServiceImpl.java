package com.sosim.server.jwt;

import com.sosim.server.jwt.dao.JwtDao;
import com.sosim.server.jwt.dao.RedisUser;
import com.sosim.server.jwt.util.property.JwtProperties;
import com.sosim.server.user.UserRepository;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

// DB와 관련된 부분들 - redis
@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtServiceImpl implements JwtService{
    // TODO injection error fix

    private final JwtRepository jwtRepository;
    private final UserRepository userRepository;
    private final RedisUserRepository redisUserRepository;
    private final JwtProperties jwtProperties;
    private final JwtFactory jwtFactory;

    public void addRedisUser() {
        RedisUser redisUser = new RedisUser("jan", 99);
        redisUserRepository.save(redisUser);
    }

    public String practiceRedis() {
//        String refreshToken = jwtFactory.createRefreshToken();
        return new String();
    }

    @Override
    public String verifyRefreshToken(HttpServletResponse response, String refreshToken) {
        jwtRepository.findByRefreshToken(refreshToken)
            .ifPresent(jwtDao -> {
                String reIssuedRefreshToken = refreshRefreshToken(jwtDao);
                sendAccessAndRefreshToken(response, jwtFactory.createAccessToken(jwtDao.getValues("todo")),
                    reIssuedRefreshToken);
            });

        return null;
    }

    // TODO
    @Override
    public String refreshRefreshToken(JwtDao jwtDao) {
//        String reIssuedRefreshToken = jwtFactory.createRefreshToken();
//        user.updateRefreshToken(reIssuedRefreshToken);
//        userRepository.saveAndFlush(user);
//        return reIssuedRefreshToken;
        return null;
    }

    @Override
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(jwtProperties.getAccessHeader(), accessToken);
    }
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(jwtProperties.getRefreshHeader(), refreshToken);
    }



}
