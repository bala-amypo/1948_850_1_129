package com.example.demo.dto;

public class AuthResponse {
    private String message;
    public AuthResponse(String message){
        this.message=message;
    }
    public AuthResponse(){}
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    
    
}
