package com.sosim.server.user;

import com.sosim.server.config.exception.CustomException;
import com.sosim.server.oauth.dto.request.OAuth2UserInfoRequest;
import com.sosim.server.type.CodeType;
import com.sosim.server.type.SocialType;
import com.sosim.server.type.UserType;
import com.sosim.server.type.WithdrawalGroundsType;
import com.sosim.server.user.dto.req.UserWithdrawalReq;
import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public User save(SocialType socialType, OAuth2UserInfoRequest oAuth2UserInfoRequest) {

        User user = User.builder()
            .socialType(socialType)
            .socialId(oAuth2UserInfoRequest.getOAuth2Id())
            .userType(UserType.ACTIVE).build();

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
        User user = userRepository.findById(id)
            .orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_USER));
        if(user.getUserType().equals(UserType.WITHDRAWAL)) {
            throw new CustomException(CodeType.USER_ALREADY_WITHDRAWAL);
        }
        return user;
    }

    @Override
    public List<User> getUserList() {
        return null;
    }

    @Override
    public void withdrawalUser(UserWithdrawalReq userWithdrawalReq) {

        User user = userRepository.findById(userWithdrawalReq.getUserId())
            .orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_USER));
        if(user.getUserType().equals(UserType.WITHDRAWAL)) {
            throw new CustomException(CodeType.USER_ALREADY_WITHDRAWAL);
        }
        user.setWithdrawalDate(LocalDateTime.now());
        user.setUserType(UserType.WITHDRAWAL);
        user.setWithdrawalGroundsType(WithdrawalGroundsType.getType(userWithdrawalReq.getWithdrawalGroundsType()));
        userRepository.save(user);
    }
}
