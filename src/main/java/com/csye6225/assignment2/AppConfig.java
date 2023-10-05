package com.csye6225.assignment2;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.csye6225.assignment2.model.UserAccount;
import com.csye6225.assignment2.service.UserAccountService;
import com.csye6225.assignment2.util.JWTUtil;

import jakarta.annotation.PostConstruct;

@Configuration
public class AppConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTUtil jwtUtil() {
        return new JWTUtil();
    }

    @Autowired
    private UserAccountService userAccountService;

    @PostConstruct
    public void loadUsersFromCSV() throws IOException {
        List<UserAccount> userAccounts = userAccountService.loadUsersFromCSV("users.csv");
        userAccountService.saveAll(userAccounts);
    }

}
