package com.example.demo.service;

import com.example.demo.model.User;

public interface AuthService {

    User registerUser(User user);

    String loginUser(String email, String password);

}
