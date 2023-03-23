package com.sosim.server.user;

import com.sosim.server.oauth.dto.request.OAuth2UserInfoRequest;
import com.sosim.server.type.SocialType;
import com.sosim.server.user.dto.req.UserWithdrawalReq;
import java.util.List;

public interface UserService {
    User save(User user, OAuth2UserInfoRequest oAuth2UserInfoRequest);
    User update(User user, OAuth2UserInfoRequest oAuth2UserInfoRequest);
    User getUser(long id);
    List<User> getUserList();
    void withdrawalUser(UserWithdrawalReq userWithdrawalReq);
}
