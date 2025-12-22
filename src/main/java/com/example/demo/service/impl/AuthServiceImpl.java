package com.example.demo.service.impl;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public String login(String username, String password) {

        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return "Invalid credentials";
        }

        if (!password.equals(user.getPassword())) {
            return "Invalid credentials";
        }

        return "Login successful";
    }
}
