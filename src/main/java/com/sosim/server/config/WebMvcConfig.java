package com.sosim.server.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3090")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);

        registry.addMapping("/login/**")
                .allowedOrigins("http://localhost:3090")
                .allowedMethods("GET", "POST")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }
}
