package com.example.demo.service;

import com.example.demo.model.User;
import java.util.Optional;

public interface AuthService {
    Optional<User> findByEmail(String email);
    User createUser(User user);
}