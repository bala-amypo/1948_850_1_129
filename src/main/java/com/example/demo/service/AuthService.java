package com.example.demo.service;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
@Service

public interface AuthService {
    AuthResponse login(AuthRequest request);
}
