package com.example.demo.security;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Simple JWT-like token provider (no external libraries).
 */
public class JwtTokenProvider {

    private final String secret;
    private final long validity; // milliseconds

    // In-memory token store
    private final Map<String, Map<String, Object>> tokenStore = new HashMap<>();

    // ✅ REQUIRED constructor
    public JwtTokenProvider(String secret, long validity) {
        this.secret = secret;
        this.validity = validity;
    }

    // ✅ GENERATE TOKEN
    public String generateToken(String email, String role, Long userId) {

        String token = UUID.randomUUID().toString();

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("role", role);
        claims.put("userId", userId);
        claims.put("expiry", System.currentTimeMillis() + validity);

        tokenStore.put(token, claims);
        return token;
    }

    // ✅ VALIDATE TOKEN
    public boolean validateToken(String token) {
        if (!tokenStore.containsKey(token)) {
            return false;
        }
        long expiry = (long) tokenStore.get(token).get("expiry");
        return System.currentTimeMillis() < expiry;
    }

    public String getEmail(String token) {
        return (String) tokenStore.get(token).get("email");
    }

    public String getRole(String token) {
        return (String) tokenStore.get(token).get("role");
    }

    public Long getUserId(String token) {
        return (Long) tokenStore.get(token).get("userId");
    }
}
