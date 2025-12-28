package com.example.demo.service.impl;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository,
                           JwtUtil jwtUtil,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------------- REGISTER ----------------
    @Override
    public User register(User user) {

        userRepository.findByEmail(user.getEmail())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Email already exists");
                });

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user
        User savedUser = userRepository.save(user);

        // ✅ CORRECT TOKEN GENERATION
        String token = jwtUtil.createToken(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole()
        );

        savedUser.setToken(token);
        return savedUser;
    }

    // ---------------- LOGIN ----------------
    @Override
    public User login(User user) {

        User existingUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        // ✅ CORRECT TOKEN GENERATION
        String token = jwtUtil.createToken(
                existingUser.getId(),
                existingUser.getEmail(),
                existingUser.getRole()
        );

        existingUser.setToken(token);
        return existingUser;
    }
}
