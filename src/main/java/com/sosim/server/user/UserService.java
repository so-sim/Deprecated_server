package com.sosim.server.user;

import com.sosim.server.oauth.dto.request.OAuth2UserInfoRequest;
import com.sosim.server.user.dto.req.UserWithdrawalReq;
import java.util.List;

public interface UserService {
    User save(User user, OAuth2UserInfoRequest oAuth2UserInfoRequest);
    User update(OAuth2UserInfoRequest oAuth2UserInfoRequest);
    User getUser(long id);
    User getUser(OAuth2UserInfoRequest userInfo);
    List<User> getUserList();
    void withdrawalUser(UserWithdrawalReq userWithdrawalReq);
}
