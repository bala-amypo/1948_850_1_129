package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    // ------------------- REGISTER -------------------
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        // Register user and generate token
        User savedUser = service.register(user);
        return ResponseEntity.ok(savedUser);
    }

    // ------------------- LOGIN -------------------
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        // Login user and generate token
        User loggedInUser = service.login(user);
        return ResponseEntity.ok(loggedInUser);
    }
}
