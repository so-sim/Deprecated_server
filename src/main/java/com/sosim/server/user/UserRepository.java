package com.sosim.server.user;

import com.sosim.server.type.SocialType;
import com.sosim.server.type.UserType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
    Optional<User> findByIdAndUserType(long id, UserType userType);
}
