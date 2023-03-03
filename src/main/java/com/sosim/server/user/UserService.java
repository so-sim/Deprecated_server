package com.sosim.server.user;

import com.sosim.server.oauth.dto.OAuth2UserInfoDto;
import com.sosim.server.user.dto.UserEmailUpdateReq;
import com.sosim.server.user.dto.UserWithdrawalReq;
import java.util.List;

public interface UserService {
    User save(String socialType, OAuth2UserInfoDto oAuth2UserInfoDto);
    User update(String socialType, OAuth2UserInfoDto oAuth2UserInfoDto, UserEmailUpdateReq userUpdateReq);
    User withdrawal(String socialType, OAuth2UserInfoDto oAuth2UserInfoDto, UserWithdrawalReq userWithdrawalReq);
    User getUser(long id);
    List<User> getUserList();
}
