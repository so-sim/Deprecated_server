package com.sosim.server.security;

import com.sosim.server.user.Users;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Data
@Builder
public class AuthUser implements UserDetails, OAuth2User {
    private final String id;
    private final String socialType;
    private final Collection<GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getName() {
        return id;
    }

    public static AuthUser create(Users user) {
        return AuthUser.builder()
                .id(user.getSocialId())
                .socialType(user.getSocialType())
                .authorities(Collections.singleton(new SimpleGrantedAuthority("USER")))
                .build();
    }

    public static AuthUser create(Users user, Map<String, Object> attributes) {
        AuthUser authUser = create(user);
        authUser.setAttributes(attributes);

        return authUser;
    }
}
