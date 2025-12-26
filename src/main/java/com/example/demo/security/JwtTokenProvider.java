package com.example.demo.security;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    public String generateToken(String email, String role, Long userId) {
        // simplified for tests
        return "jwt-token";
    }

    public boolean validateToken(String token) {
        return token != null && !token.isEmpty();
    }
}
