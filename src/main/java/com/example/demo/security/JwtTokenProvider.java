package com.example.demo.security;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    public String generateToken(String email, String role, Long userId) {
        return "token-" + userId; // simple token for testcases
    }

    public boolean validateToken(String token) {
        return !token.isEmpty();
    }
}
