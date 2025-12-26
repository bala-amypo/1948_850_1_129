package com.example.demo.service;

public interface AuthService {

    String login(String email, String password);

    boolean validateToken(String token);
}
