package com.sosim.server.user;

import static com.sosim.server.type.UserType.WITHDRAWAL;

import com.sosim.server.oauth.dto.OAuth2UserInfoDto;
import com.sosim.server.type.SocialType;
import com.sosim.server.type.UserType;
import com.sosim.server.type.WithdrawalGroundsType;
import com.sosim.server.user.dto.req.UserWithdrawalReq;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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
//        return userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCodeType.NOT_FOUND_USER));
        return userRepository.findById(id).get();
    }

    @Override
    public List<User> getUserList() {
        return null;
    }

    //TODO 이미 탈퇴한 회원입니다 추가
    //TODO customError부분 바꾸기
    @Override
    public void withdrawalUser(UserWithdrawalReq userWithdrawalReq) {
//        User user = userRepository.findById(userWithdrawalReq.getUserId())
//            .orElseThrow(() -> new CustomException(ErrorCodeType.NOT_FOUND_USER));
        User user = userRepository.findById(userWithdrawalReq.getUserId())
            .get();
        user.setWithdrawalDate(LocalDateTime.now());
        user.setUserType(WITHDRAWAL);
        user.setWithdrawalGroundsType(WithdrawalGroundsType.getType(userWithdrawalReq.getWithdrawalGroundsType()));
        userRepository.save(user);
    }
}
