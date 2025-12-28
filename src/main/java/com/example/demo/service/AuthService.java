package com.example.demo.service;

import com.example.demo.model.User;

public interface AuthService {

    User register(User user);

    User login(User user);
}
