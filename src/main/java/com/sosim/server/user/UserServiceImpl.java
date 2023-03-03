package com.sosim.server.user;

import com.sosim.server.config.exception.CustomException;
import com.sosim.server.oauth.dto.OAuth2UserInfoDto;
import com.sosim.server.type.ErrorCodeType;
import com.sosim.server.type.UserType;
import com.sosim.server.user.dto.UserEmailUpdateReq;
import com.sosim.server.user.dto.UserWithdrawalReq;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public User save(String socialType, OAuth2UserInfoDto oAuth2UserInfoDto) {

        User user = User.builder()
            .createDate(LocalDateTime.now())
            .socialType(socialType)
            .socialId(oAuth2UserInfoDto.getOAuth2Id())
            .userType(UserType.ACTIVE).build();

        // email있어야 email넣기
        if(!oAuth2UserInfoDto.getEmail().isEmpty()) {
            user.setEmail(oAuth2UserInfoDto.getEmail());
        }

        userRepository.save(user);
        return user;
    }

    @Override
    public User update(String socialType, OAuth2UserInfoDto oAuth2UserInfoDto, UserEmailUpdateReq userUpdateReq) {
        User user = userRepository.findBySocialTypeAndSocialId(socialType, oAuth2UserInfoDto.getOAuth2Id())
            .orElseThrow(() -> new CustomException(ErrorCodeType.NOT_FOUND_USER));
        user.setEmail(userUpdateReq.getEmail());
        userRepository.save(user);
        return user;
    }

    @Override
    public User withdrawal(String socialType, OAuth2UserInfoDto oAuth2UserInfoDto, UserWithdrawalReq userWithdrawalReq) {
        User user = userRepository.findBySocialTypeAndSocialId(socialType, oAuth2UserInfoDto.getOAuth2Id())
            .orElseThrow(() -> new CustomException(ErrorCodeType.NOT_FOUND_USER));
        user.setWithdrawalDate(LocalDateTime.now());
        user.setWithdrawalGroundsType(userWithdrawalReq.getWithdrawalGroundsType());
        userRepository.save(user);
        return user;
    }

    @Override
    public User getUser(long id) {
        return userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCodeType.NOT_FOUND_USER));
    }

    @Override
    public List<User> getUserList() {
        return null;
    }
}
