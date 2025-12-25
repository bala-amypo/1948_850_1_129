package com.example.demo.service;

import com.example.demo.dto.AuthRequest;
import com.example.demo.model.User;

public interface AuthService {

    User register(User user);

    String login(AuthRequest request);
}
