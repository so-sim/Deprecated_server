package com.sosim.server.user;

import com.sosim.server.config.exception.CustomException;
import com.sosim.server.oauth.dto.OAuth2UserInfoDto;
import com.sosim.server.type.ErrorCodeType;
import com.sosim.server.type.SocialType;
import com.sosim.server.type.UserType;
import com.sosim.server.type.WithdrawalGroundsType;
import com.sosim.server.user.dto.req.UserWithdrawalReq;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public User save(SocialType socialType, OAuth2UserInfoDto oAuth2UserInfoDto) {

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
    public User update(User user, OAuth2UserInfoDto oAuth2UserInfoDto) {
        user.setEmail(oAuth2UserInfoDto.getEmail());
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

    @Override
    public void withdrawalUser(UserWithdrawalReq userWithdrawalReq) {
        User user = userRepository.findById(userWithdrawalReq.getUserId())
            .orElseThrow(() -> new CustomException(ErrorCodeType.NOT_FOUND_USER));
        user.setWithdrawalDate(LocalDateTime.now());
        user.setWithdrawalGroundsType(WithdrawalGroundsType.getType(userWithdrawalReq.getWithdrawalGroundsType()));
        userRepository.save(user);
    }
}
