package com.csye6225.assignment2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.csye6225.assignment2.service.UserAccountService;
import com.csye6225.assignment2.util.JWTUtil;

@Configuration

public class SecurityConfig {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private JWTUtil jwtUtil;

    public SecurityConfig(UserAccountService userAccountService, JWTUtil jwtUtil) {
        this.userAccountService = userAccountService;
        this.jwtUtil = jwtUtil;
    }

}