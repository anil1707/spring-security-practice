package com.example.spring_security_demo.dto;

public class UserResponseDto {
    private String name;
    private String email;

    public UserResponseDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
