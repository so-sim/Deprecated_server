package com.sosim.server.jwt;

import static com.sosim.server.jwt.util.constant.CustomConstant.REFRESH_TOKEN_KEY;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sosim.server.jwt.dao.JwtDao;
import com.sosim.server.jwt.util.property.JwtProperties;
import com.sosim.server.user.UserRepository;
import java.time.Duration;
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
    private final JwtRepository jwtRepository;
    private final UserRepository userRepository;
    private final RedisUserRepository redisUserRepository;
    private final JwtProperties jwtProperties;
    private final JwtFactory jwtFactory;
    private final JwtDao jwtDao;
    private final ObjectMapper objectMapper;

    @Override
    public void saveRefreshToken(String accessToken) {
        jwtDao.setValues(REFRESH_TOKEN_KEY, jwtFactory.createRefreshToken(accessToken), Duration.ofMillis(
            jwtProperties.getRefreshTokenExpirationPeriod()));
    }

    @Override
    public String verifyRefreshToken(HttpServletResponse response, String refreshToken) {
        jwtRepository.findByRefreshToken(refreshToken)
            .ifPresent(jwtDao -> {
                String reIssuedRefreshToken = refreshRefreshToken(jwtDao);
                // 찾아서 id랑 email가져올것
                sendAccessAndRefreshToken(response, jwtFactory.createAccessToken(1L, new String()),
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
