package com.skillswap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean; // Import this
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Import this
import org.springframework.security.crypto.password.PasswordEncoder; // Import this

@SpringBootApplication
public class SkillswapApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkillswapApplication.class, args);
    }

}