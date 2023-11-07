package com.bancosangue.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class DatabaseConnectionTest {

   public static void main(String[] args) {
        SpringApplication.run(DatabaseConnectionTest.class, args);
    }

    @Bean
    public CommandLineRunner testDatabaseConnection(JdbcTemplate jdbcTemplate) {
        return (args) -> {
            try {
                String result = jdbcTemplate.queryForObject("SELECT 'Database connection successful'", String.class);
                System.out.println("Database Connection Test Result: " + result);
            } catch (Exception e) {
                System.err.println("Database connection failed. Error: " + e.getMessage());
            }
        };
    }
}