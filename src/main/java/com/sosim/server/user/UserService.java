package com.sosim.server.user;

import com.sosim.server.oauth.dto.OAuth2UserInfoDto;
import com.sosim.server.type.SocialType;
import com.sosim.server.user.dto.req.UserWithdrawalReq;
import java.util.List;

public interface UserService {
    User save(SocialType socialType, OAuth2UserInfoDto oAuth2UserInfoDto);
    User update(User user, OAuth2UserInfoDto oAuth2UserInfoDto);
    User getUser(long id);
    List<User> getUserList();
    void withdrawalUser(UserWithdrawalReq userWithdrawalReq);
}
