package com.sosim.server.jwt;

import static com.sosim.server.jwt.util.constant.CustomConstant.REFRESH_TOKEN_KEY;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sosim.server.jwt.dao.JwtDao;
import com.sosim.server.jwt.util.property.JwtProperties;
import com.sosim.server.user.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

// DB와 관련된 부분들 - redis
@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtServiceImpl implements JwtService{
    private final JwtRepository jwtRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtProperties jwtProperties;
    private final JwtFactory jwtFactory;
    private final JwtProvider jwtProvider;
    private final JwtDao jwtDao;
    private final ObjectMapper objectMapper;

    // TODO id, email, refreshTokenValue, REFRESH_TOKEN_KEY사용해서 하는것으로 수정
    @Override
    public void saveRefreshToken() {
//        jwtDao.setValues(REFRESH_TOKEN_KEY, jwtFactory.createRefreshToken(), Duration.ofMillis(
//            jwtProperties.getRefreshTokenExpirationPeriod()));
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


    /**
     *  1. 헤더에서 추출한 RefreshToken으로 redis에서 유저 정보를 탐색
     *  2. 유저가 있다면 AccessToken 생성, refreshToken 재발급 & redis에 refreshToken 업데이트
     *  3. AccessToken과 RefreshToken 응답 헤더에 실어 보내기
     */
    @Override
    public Map<HttpServletRequest, HttpServletResponse> verifyRefreshTokenAndReIssueAccessToken(HttpServletRequest request, HttpServletResponse response, String refreshToken) {

        // TODO 이렇게 하는것이 맞는지 고찰-레디스에 집어넣는 데이터 이런식이 맞을지?
        // : id와 email로 accessToken을 만들어야 하니 레디스에 해당 값이 있어야 할듯
        // 1. 간단하게 refreshToken값을 id로 해서 value에 id를 저장하고 id로 email을 꺼내오거나
        // 2. refreshToken, id, email이렇게 일렬로 된 데이터를 저장 :
        // 맵 형태로 저장 : key-id, value-[refreshToken, email]

        // 1.
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        String refreshTokenValue = valueOperations.get(REFRESH_TOKEN_KEY);
//        log.info("redis RefreshTokenKey : {}", valueOperations.get(refreshTokenValue));

        // TODO 2. 실험한 내용, test code로 이동시킬것
        // 이런식으로 만든 곳에서 꺼내서
//        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
//        Map<String, Object> map = new HashMap<>();
//        List<String> list1 = new ArrayList<>();
//        list1.add("id1");
//        list1.add( "email1");
//        map.put(refreshTokenValue, list1);
//        List<String> list2 = new ArrayList<>();
//        list2.add("id2");
//        list2.add( "email2");
//        map.put("refreshTokenValue2", list2);
//        hashOperations.putAll(REFRESH_TOKEN_KEY, map);

//        List<String> idEmailList = new ArrayList<>();
//        idEmailList = (List) redisTemplate.opsForHash().get(REFRESH_TOKEN_KEY, refreshToken);
        List<String> idEmailList = jwtDao.getHashes(REFRESH_TOKEN_KEY, refreshToken);
        String id = idEmailList.get(0);
        String email = idEmailList.get(1);

        if (refreshToken != null) {
            String reIssuedRefreshToken = jwtProvider.reIssueRefreshToken(id, email, refreshToken);
            sendAccessAndRefreshToken(response, jwtFactory.createAccessToken(id, email), reIssuedRefreshToken);
        }

        Map<HttpServletRequest, HttpServletResponse> requestAndResponse = new HashMap<>();
        requestAndResponse.put(request, response);
        return requestAndResponse;
    }

    @Override
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        jwtProvider.setAccessTokenHeader(response, accessToken);
        jwtProvider.setRefreshTokenHeader(response, refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }

    // TODO service에 놓을지 provider에 놓을지
//    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
//        response.setHeader(jwtProperties.getAccessHeader(), accessToken);
//    }
    // TODO service에 놓을지 provider에 놓을지
//    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
//        response.setHeader(jwtProperties.getRefreshHeader(), refreshToken);
//    }



}
