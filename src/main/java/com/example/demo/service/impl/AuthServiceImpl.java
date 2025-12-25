package com.example.demo.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    // Hardcoded user for demo; replace with DB if needed
    private final String ADMIN_EMAIL = "admin";
    private final String ADMIN_PASSWORD_HASH; // store encoded password

    public AuthServiceImpl(JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.ADMIN_PASSWORD_HASH = passwordEncoder.encode("admin123");
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        if (!ADMIN_EMAIL.equals(request.getEmail()) ||
            !passwordEncoder.matches(request.getPassword(), ADMIN_PASSWORD_HASH)) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = jwtTokenProvider.generateToken(ADMIN_EMAIL, "ADMIN", 1L);
        return new AuthResponse(token);
    }
}
