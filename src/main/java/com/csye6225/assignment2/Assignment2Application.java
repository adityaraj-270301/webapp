package com.csye6225.assignment2;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@ComponentScan({ "com.csye6225.assignment2.controllers", "com.csye6225.assignment2.util",
		"com.csye6225.assignment2.service", "com.csye6225.assignment2.model" })
@Import(AppConfig.class)
public class Assignment2Application {

	public static void main(String[] args) {
		SpringApplication.run(Assignment2Application.class, args);
		// SpringApplication.run(AppConfig.class, args);
	}

}
