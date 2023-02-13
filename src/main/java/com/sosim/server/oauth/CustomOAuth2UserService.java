package com.sosim.server.oauth;


import com.sosim.server.oauth.dto.OAuth2UserInfoDto;
import com.sosim.server.security.AuthUser;
import com.sosim.server.user.Users;
import com.sosim.server.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return process(userRequest, oAuth2User);
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfoDto userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());

        Users user = saveOrUpdate(registrationId, userInfo);

        return AuthUser.create(user, userInfo.getAttributes());
    }

    private Users saveOrUpdate(String registrationId, OAuth2UserInfoDto userInfo) {
        Users user = userRepository.findBySocialId(userInfo.getId());
        if (user == null) {
            user = Users.builder()
                    .nickname(userInfo.getName())
                    .email(userInfo.getEmail())
                    .createDate(LocalDateTime.now())
                    .socialType(registrationId)
                    .socialId(userInfo.getId())
                    .build();
        } else if (!user.getNickname().equals(userInfo.getName()) || !user.getEmail().equals(userInfo.getEmail())) {
//            user.update(userInfo);
        }

        return userRepository.save(user);
    }
}
