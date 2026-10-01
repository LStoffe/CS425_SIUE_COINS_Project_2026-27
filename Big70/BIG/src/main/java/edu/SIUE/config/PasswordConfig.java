package edu.SIUE.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * PasswordConfig.java
 *
 * This configuration class provides a BCryptPasswordEncoder bean for secure
 * password hashing and verification within the application. It is used by
 * both admin and company authentication components.
 *
 * Do not remove this configuration; it is essential for Spring Security 
 * password management for all user types.
 */

@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
