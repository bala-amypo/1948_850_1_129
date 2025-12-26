package com.example.demo.controller;

import com.example.demo.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email,
                                        @RequestParam String password) {
        String token = authService.login(email, password);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestParam String token) {
        return ResponseEntity.ok(authService.validateToken(token));
    }
}
