package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public LoginResponse login(LoginRequest request) {

        if ("admin".equals(request.getUsername())
                && "admin123".equals(request.getPassword())) {

            return new LoginResponse("Login successful");
        }

        return new LoginResponse("Invalid credentials");
    }
}
