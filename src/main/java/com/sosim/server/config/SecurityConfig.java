package com.sosim.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF
                .csrf().disable()

                .httpBasic().disable()

                // 세션 STATELESS 설정
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 요청에 대한 권한 체크 파트
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll();

        // Jwt 인증 필터 -> UsernamePassword 필터 전에 추가, 추후 코드 보면서 수정
//                .and()
//                .addFilterBefore(new AuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
