package com.example.aptech.spring.library.ValidationException;

import jakarta.servlet.ServletException;

public class JwtExpiredException extends ServletException {
    public JwtExpiredException(String message) {
        super(message);
    }
}


