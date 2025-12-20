package com.example.demo.service.impl;

import com.example.demo.dto.*;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.AuthService;
@RestController

public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider tokenProvider = new JwtTokenProvider();

    @Override
    public AuthResponse login(AuthRequest request) {
        String token = tokenProvider.generateToken(
                request.getEmail(), "USER", 1L
        );
        return new AuthResponse(token);
    }
}
