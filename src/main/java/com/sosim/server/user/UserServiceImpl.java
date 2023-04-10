package com.sosim.server.user;

import com.sosim.server.config.exception.CustomException;
import com.sosim.server.group.GroupRepository;
import com.sosim.server.oauth.dto.request.OAuth2UserInfoRequest;
import com.sosim.server.participant.Participant;
import com.sosim.server.participant.ParticipantRepository;
import com.sosim.server.participant.ParticipantService;
import com.sosim.server.type.CodeType;
import com.sosim.server.type.StatusType;
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
    private final ParticipantRepository participantRepository;
    private final GroupRepository groupRepository;
    private final ParticipantService participantService;

    @Override
    public User save(User user, OAuth2UserInfoRequest oAuth2UserInfoRequest) {
        if (userRepository.findBySocialTypeAndSocialIdAndUserType(oAuth2UserInfoRequest.getOAuth2SocialType(),
                oAuth2UserInfoRequest.getOAuth2Id(), UserType.ACTIVE).isPresent()) {
            throw new CustomException(CodeType.USER_ALREADY_EXIST);
        }

        return userRepository.save(user);
    }

    public User update(OAuth2UserInfoRequest oAuth2UserInfoRequest) {
        User user = getUser(oAuth2UserInfoRequest);
        if(!user.getEmail().equals(oAuth2UserInfoRequest.getEmail())) {
            user.setEmail(oAuth2UserInfoRequest.getEmail());
        }

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
    public User getUser(OAuth2UserInfoRequest userInfo) {
        return userRepository.findBySocialTypeAndSocialIdAndUserType(userInfo.getOAuth2SocialType(),
                        userInfo.getOAuth2Id(), UserType.ACTIVE)
                .orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_USER));
    }

    @Override
    public List<User> getUserList() {
        return null;
    }

    @Override
    public void withdrawalUser(UserWithdrawalReq userWithdrawalReq) {

        User user = userRepository.findById(userWithdrawalReq.getUserId())
            .orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_USER));
        if (user.getUserType().equals(UserType.WITHDRAWAL)) {
            throw new CustomException(CodeType.USER_ALREADY_WITHDRAWAL);
        }
        if (groupRepository.findByAdminIdAndStatusType(user.getId(), StatusType.ACTIVE).isPresent()) {
            throw new CustomException(CodeType.CANNOT_WITHDRAWAL_BY_GROUP_ADMIN);
        }
        List<Participant> participantList = participantRepository.findByUserAndStatusType(user, StatusType.ACTIVE);
        if (participantList != null) {
            for (Participant participant : participantList) {
                participantService.deleteParticipantEntity(participant.getUser(), participant.getGroup());
            }
        }
        user.setWithdrawalDate(LocalDateTime.now());
        user.setUserType(UserType.WITHDRAWAL);
        user.setWithdrawalGroundsType(WithdrawalGroundsType.getType(userWithdrawalReq.getWithdrawalGroundsType()));
        userRepository.save(user);
    }
}
