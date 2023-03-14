package com.sosim.server.user;

import com.sosim.server.security.AuthUser;
import org.springframework.security.core.Authentication;

public interface AuthService {

    AuthUser getSessionInfo(Authentication authentication);

}
