package com.example.demo.model;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String role;

    /**
     * Token is auto-generated and returned in response only.
     * Swagger will NOT ask this as input.
     */
    @Transient
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String token;

    // ================= CONSTRUCTORS =================

    public User() {
    }

    public User(Long id, String email, String password, String role) {
        this.id = id;
        setEmail(email);
        this.password = password;
        this.role = role;
    }

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    // ✅ FIXED EMAIL VALIDATION
    public void setEmail(String email) {
        if (email == null ||
            !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email");
        }
        this.email = email.toLowerCase();
    }

    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    // ✅ Default role safety
    public void setRole(String role) {
        this.role = (role == null || role.isBlank()) ? "USER" : role;
    }

    // ================= TOKEN =================

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
