package com.example.demo.security;

public class JwtTokenProvider {

    public String generateToken(String email, String role, Long userId) {
        if (email == null || role == null || userId == null) {
            return null;
        }
        return email + ":" + role + ":" + userId;
    }

    public boolean validateToken(String token) {
        return token != null && token.split(":").length == 3;
    }
}
