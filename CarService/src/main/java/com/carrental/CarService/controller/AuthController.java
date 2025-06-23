package com.carrental.CarService.controller;

import com.carrental.CarService.model.User;
import com.carrental.CarService.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        logger.info("Received registration request for username: {}", user.getUsername());
        try {
            User newUser = userService.createUser(user);
            logger.info("User registered successfully: {}", newUser.getUsername());
            return ResponseEntity.ok(newUser);
        } catch (IllegalArgumentException e) {
            logger.warn("Registration failed for username {}: {}", user.getUsername(), e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during registration for username {}: {}", user.getUsername(), e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        logger.info("Login attempt for username: {}", user.getUsername());
        try {
            User newUser = userService.loginUser(user);
            if (newUser == null) {
                logger.warn("Login failed for username: {}", user.getUsername());
                return ResponseEntity.status(401).body("Login unsuccessful");
            }
            logger.info("Login successful for username: {}", newUser.getUsername());
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            logger.error("Unexpected error during login for username {}: {}", user.getUsername(), e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> payload) {
        String idToken = payload.get("idToken");
        logger.info("Google login request received");
        try {
            User user = userService.loginWithGoogle(idToken);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.error("Google login failed: {}", e.getMessage(), e);
            return ResponseEntity.status(401).body("Google login failed");
        }
    }

    @PostMapping("/google-signup")
    public ResponseEntity<?> signupWithGoogle(@RequestBody Map<String, String> payload) {
        String idToken = payload.get("idToken");
        logger.info("Google signup request received");
        try {
            User user = userService.signupWithGoogle(idToken);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.error("Google signup failed: {}", e.getMessage(), e);
            return ResponseEntity.status(400).body("Google signup failed: " + e.getMessage());
        }
    }

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard() {
        logger.info("Accessed admin dashboard");
        return "Welcome Admin!";
    }

    @GetMapping("/user/dashboard")
    @PreAuthorize("hasRole('USER')")
    public String userDashboard() {
        logger.info("Accessed user dashboard");
        return "Welcome User!";
    }
}
