package com.sosim.server.user;

import com.sosim.server.type.SocialType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // TODO 재민님 작업 merge하고 Provider(만들어 두신 enum)으로 바꾸기
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}
