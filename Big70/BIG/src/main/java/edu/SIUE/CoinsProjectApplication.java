package edu.SIUE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CoinsProjectApplication.java
 *
 * Main entry point for the COINS Spring Boot application.
 *
 * This class bootstraps the Spring context and launches the application.
 *
 * Responsibilities:
 *   - Serves as the primary application runner for Spring Boot.
 *   - Initializes all Spring Beans and auto-configuration.
 *   - Kicks off the embedded server and starts the application context.
 *
 * Usage:
 *   - Run this class to start the web server (e.g., via IDE or command line: `mvn spring-boot:run`).
 *   - Main method will delegate control to Spring Boot's auto configuration.
 * 
 */

@SpringBootApplication
public class CoinsProjectApplication {
    
    // Main method to start the Spring Boot application.
    public static void main(String[] args) {

        // Launches the Spring Boot application
        SpringApplication.run(CoinsProjectApplication.class, args);
    }
}