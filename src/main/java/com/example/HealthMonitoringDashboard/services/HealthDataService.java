package com.example.HealthMonitoringDashboard.services;

import org.springframework.stereotype.Service;

@Service
public class HealthDataService {

    public double calculateBMI(double weight, double height) {
        // BMI formula: weight (kg) / [height (m)]^2
        return weight / Math.pow(height / 100, 2);
    }

    public String getHealthStatus(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi >= 18.5 && bmi < 25) {
            return "Healthy";
        } else if (bmi >= 25 && bmi < 30) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    public String getHealthTip(double bmi) {
        if (bmi < 18.5) {
            return "Consider eating more nutrient-dense foods to gain weight.";
        } else if (bmi >= 25) {
            return "Incorporate regular exercise and balanced meals into your routine.";
        } else {
            return "Keep up the good work and maintain a healthy lifestyle!";
        }
    }
}
