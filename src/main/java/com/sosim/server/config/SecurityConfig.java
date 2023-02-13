package com.sosim.server.config;

import com.sosim.server.jwt.JwtProvider;
import com.sosim.server.jwt.JwtProviderImpl;
import com.sosim.server.oauth.CustomOAuth2UserService;
import com.sosim.server.security.authentication.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // csrf
                .httpBasic().disable()
                .headers().frameOptions().disable();

        // 세션 STATELESS 설정
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 요청에 대한 권한 체크 파트
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api/hello").hasRole("USER");

        // Jwt 인증 필터
        http
                .addFilterBefore(new AuthenticationFilter(new JwtProviderImpl()), UsernamePasswordAuthenticationFilter.class);

        http
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService);

        return http.build();
    }
}
