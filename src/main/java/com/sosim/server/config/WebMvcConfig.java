package com.sosim.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3090")
                .allowedOrigins("https://sosim-manager.com")
                .allowedMethods("GET", "POST", "PATCH", "OPTIONS", "DELETE", "PUT")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
