package com.example.demo.service.impl;

import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(String email, String password) {
        // In real app: validate email & password from DB
        // Here: minimal logic to support JWT generation

        String role = "CUSTOMER";
        Long userId = 1L;

        return jwtTokenProvider.generateToken(email, role, userId);
    }

    @Override
    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }
}
