package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public AuthResponse login(AuthRequest request) {

        if ("admin".equals(request.getUsername())
                && "admin123".equals(request.getPassword())) {

            return new AuthResponse("Login successful");
        }

        return new AuthResponse("Invalid credentials");
    }
}
