package com.example.HealthMonitoringDashboard.controllers;

import com.example.HealthMonitoringDashboard.models.User;
import com.example.HealthMonitoringDashboard.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        if (userRepository.findByUsername(username) != null) {
            return "redirect:/register?error=Username already exists!";
        }

        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return "redirect:/register-details?username=" + username;
    }

    @GetMapping("/register-details")
    public String showRegisterDetailsPage(@RequestParam("username") String username, Model model) {
        // Log for debugging
        System.out.println("Username received in GET /register-details: " + username);

        // Add username to the model
        model.addAttribute("username", username.trim());

        return "register-details"; // Render register-details.html
    }


    @PostMapping("/register-details")
    public String completeRegistration(
            @RequestParam("username") String username,
            @RequestParam("name") String name,
            @RequestParam("age") int age,
            @RequestParam("gender") String gender,
            @RequestParam("weight") double weight,
            @RequestParam("height") double height,
            @RequestParam("medicalHistory") String medicalHistory,
            Model model) {
        // Log received data for debugging
        System.out.println("Username received in POST /register-details: " + username);
        System.out.println("Name: " + name + ", Age: " + age + ", Gender: " + gender);
        System.out.println("Weight: " + weight + ", Height: " + height);
        System.out.println("Medical History: " + medicalHistory);

        // Fetch the user by username
        User user = userRepository.findByUsername(username.trim());
        if (user == null) {
            System.out.println("User not found for username: " + username);
            return "redirect:/register?error=User not found!";
        }

        // Update user details
        user.setName(name);
        user.setAge(age);
        user.setGender(gender);
        user.setWeight(weight);
        user.setHeight(height);
        user.setMedicalHistory(medicalHistory);

        // Save updated user to the database
        userRepository.save(user);

        System.out.println("User details updated successfully!");

        // Redirect to login
        model.addAttribute("message", "Details saved successfully! Redirecting to login...");
        return "register-details-success";
    }

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password.");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        return "login";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        // Fetch the currently authenticated user's username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Fetch user details from the database
        User user = userRepository.findByUsername(username);

        if (user != null) {
            // Pass the entire user object to the view
            model.addAttribute("user", user);
        } else {
            // Handle the case where the user is not found
            model.addAttribute("error", "User not found.");
        }

        return "profile"; // Render profile.html
    }

    @GetMapping("/profile/edit")
    public String showEditProfileForm(Model model) {
        // Fetch the currently authenticated user's username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Fetch user details from the database
        User user = userRepository.findByUsername(username);

        if (user != null) {
            model.addAttribute("user", user);
        } else {
            model.addAttribute("error", "User not found.");
            return "profile";
        }

        return "edit-profile"; // Render edit-profile.html
    }

    @PostMapping("/profile/edit")
    public String updateProfile(
            @RequestParam("name") String name,
            @RequestParam("age") int age,
            @RequestParam("gender") String gender,
            @RequestParam("weight") double weight,
            @RequestParam("height") double height,
            @RequestParam("medicalHistory") String medicalHistory) {

        // Get the authenticated user's username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Fetch user from the database
        User user = userRepository.findByUsername(username);

        if (user != null) {
            // Update user details
            user.setName(name);
            user.setAge(age);
            user.setGender(gender);
            user.setWeight(weight);
            user.setHeight(height);
            user.setMedicalHistory(medicalHistory);

            // Save updated user
            userRepository.save(user);
        }

        return "redirect:/profile"; // Redirect to the profile page
    }


}
