package com.example.HealthMonitoringDashboard.controllers;

import com.example.HealthMonitoringDashboard.models.User;
import com.example.HealthMonitoringDashboard.repositories.UserRepository;
import com.example.HealthMonitoringDashboard.services.HealthDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayInputStream;

@Controller
public class DashboardController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HealthDataService healthDataService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Get logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return "redirect:/login?error=User not found!";
        }

        // Calculate BMI and Health Status
        double bmi = healthDataService.calculateBMI(user.getWeight(), user.getHeight());
        String healthStatus = healthDataService.getHealthStatus(bmi);

        // Add attributes to the model
        model.addAttribute("user", user);
        model.addAttribute("bmi", String.format("%.2f", bmi));
        model.addAttribute("healthStatus", healthStatus);
        model.addAttribute("dailyStepsGoal", 10000);
        model.addAttribute("waterGoal", 3);
        model.addAttribute("weeklySteps", new int[]{8000, 9000, 7500, 8500, 10000, 7000, 9500});

        return "dashboard";
    }


    @GetMapping("/exportUserData")
    public ResponseEntity<InputStreamResource> exportUserData(String username) {
        User user = userRepository.findByUsername(username);
        ByteArrayInputStream stream = createPdf(user);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=user_data.pdf")
                .body(new InputStreamResource(stream));
    }

    private ByteArrayInputStream createPdf(User user) {
        // Implement PDF generation logic
        // Use a library like iText or Apache PDFBox to generate PDF from user data
        return null;
    }
}
