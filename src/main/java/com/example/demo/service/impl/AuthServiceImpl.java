package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;

import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public LoginResponse login(LoginRequest request) {
        if ("admin".equals(request.getUsername())
                && "admin".equals(request.getPassword())) {
            return new LoginResponse("Login successful", true);
        }
        return new LoginResponse("Invalid credentials", false);
    }
}
