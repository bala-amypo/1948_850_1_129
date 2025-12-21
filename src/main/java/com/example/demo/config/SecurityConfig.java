package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // Disable CSRF
            .csrf(csrf -> csrf.disable())

            // Disable default login page
            .formLogin(form -> form.disable())

            // Disable basic auth
            .httpBasic(basic -> basic.disable())

            // Allow all requests
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/health",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/api/**"
                ).permitAll()
                .anyRequest().permitAll()
            );

        return http.build();
    }
}
