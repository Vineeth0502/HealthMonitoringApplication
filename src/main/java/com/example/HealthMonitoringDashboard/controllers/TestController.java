package com.example.HealthMonitoringDashboard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.HealthMonitoringDashboard.repositories.UserRepository;

@RestController
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/test-db")
    public String testDatabaseConnection() {
        long userCount = userRepository.count();
        return "Database connection successful! Total users: " + userCount;
    }
}

