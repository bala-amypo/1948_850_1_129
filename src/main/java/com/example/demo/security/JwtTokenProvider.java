package com.example.demo.security;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtTokenProvider {

    public String generateToken(String email, String role, Long userId) {

        // Generate unique token per login
        return UUID.randomUUID().toString();
    }

    public boolean validateToken(String token) {
        return token != null && !token.isEmpty();
    }
}
