package com.sosim.server.user;

import static com.sosim.server.type.UserType.WITHDRAWAL;

import com.sosim.server.config.exception.CustomException;
import com.sosim.server.oauth.dto.request.OAuth2UserInfoRequest;
import com.sosim.server.type.CodeType;
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
    public User save(SocialType socialType, OAuth2UserInfoRequest oAuth2UserInfoRequest) {

        User user = User.builder()
            .createDate(LocalDateTime.now())
            .socialType(socialType)
            .socialId(oAuth2UserInfoRequest.getOAuth2Id())
            .userType(UserType.ACTIVE).build();

        // email있어야 email넣기
        if(!oAuth2UserInfoRequest.getEmail().isEmpty()) {
            user.setEmail(oAuth2UserInfoRequest.getEmail());
        }

        userRepository.save(user);
        return user;
    }

    @Override
    public User update(User user, OAuth2UserInfoRequest oAuth2UserInfoRequest) {
        user.setEmail(oAuth2UserInfoRequest.getEmail());
        userRepository.save(user);
        return user;
    }

    @Override
    public User getUser(long id) {
        return userRepository.findById(id).orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_USER));
    }

    @Override
    public List<User> getUserList() {
        return null;
    }

    //TODO 이미 탈퇴한 회원입니다 추가
    //TODO customError부분 바꾸기
    @Override
    public void withdrawalUser(UserWithdrawalReq userWithdrawalReq) {
        User user = userRepository.findById(userWithdrawalReq.getUserId())
            .orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_USER));
        user.setWithdrawalDate(LocalDateTime.now());
        user.setUserType(WITHDRAWAL);
        user.setWithdrawalGroundsType(WithdrawalGroundsType.getType(userWithdrawalReq.getWithdrawalGroundsType()));
        userRepository.save(user);
    }
}
