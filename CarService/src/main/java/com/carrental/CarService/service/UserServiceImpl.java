package com.carrental.CarService.service;

import com.carrental.CarService.config.JwtUtils;
import com.carrental.CarService.exception.AuthenticationFailedException;
import com.carrental.CarService.exception.UserAlreadyExistsException;
import com.carrental.CarService.model.User;
import com.carrental.CarService.repository.UserRepo;
import com.carrental.CarService.utility.GoogleTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final GoogleTokenVerifier googleTokenVerifier;

    public UserServiceImpl(UserRepo userRepo,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils,
            GoogleTokenVerifier googleTokenVerifier) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.googleTokenVerifier = googleTokenVerifier;
    }

    @Override
    public User createUser(User user) {
        logger.info("Attempting to register user: {}", user.getUsername());
        if (userRepo.findByUsername(user.getUsername()) != null) {
            logger.warn("Registration failed: Username already exists - {}", user.getUsername());
            throw new UserAlreadyExistsException("Username or Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepo.save(user);
        logger.info("User registered successfully with username: {}", savedUser.getUsername());
        return savedUser;
    }

    @Override
    public User signupWithGoogle(String idToken) {
        logger.info("Google signup attempt with ID token");

        GoogleIdToken.Payload payload = googleTokenVerifier.verifyToken(idToken);
        if (payload == null) {
            logger.warn("Invalid Google ID token");
            throw new AuthenticationFailedException("Invalid Google ID token");
        }

        String email = payload.getEmail();
        String name = (String) payload.get("name");

        if (userRepo.findByEmail(email) != null) {
            logger.warn("User already exists with email: {}", email);
            throw new UserAlreadyExistsException("User already exists. Please login.");
        }

        User newUser = new User();
        newUser.setUsername(name);
        newUser.setEmail(email);
        newUser.setUserRole("ROLE_USER");
        newUser.setPassword(""); // No password for Google signup
        userRepo.save(newUser);

        String jwtToken = jwtUtils.generateToken(newUser.getUsername());
        newUser.setToken(jwtToken);

        logger.info("New user signed up via Google: {}", email);
        return newUser;
    }

    @Override
    public User loginUser(User user) {
        logger.info("Login attempt for username: {}", user.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            if (authentication.isAuthenticated()) {
                String token = jwtUtils.generateToken(user.getUsername());
                User mainUser = userRepo.findByUsername(user.getUsername());
                mainUser.setToken(token);
                logger.info("Login successful for username: {}", mainUser.getUsername());
                return mainUser;
            } else {
                logger.warn("Authentication failed for username: {}", user.getUsername());
                throw new AuthenticationFailedException("Invalid username or password");
            }
        } catch (Exception e) {
            logger.error("Login error for username: {}", user.getUsername(), e);
            throw new AuthenticationFailedException("Login failed: " + e.getMessage());
        }
    }

    @Override
    public User loginWithGoogle(String idToken) {
        logger.info("Google login attempt with ID token");

        GoogleIdToken.Payload payload = googleTokenVerifier.verifyToken(idToken);
        if (payload == null) {
            logger.warn("Invalid Google ID token");
            throw new AuthenticationFailedException("Invalid Google ID token");
        }

        String email = payload.getEmail();
        User user = userRepo.findByEmail(email);

        if (user == null) {
            logger.warn("No user found for email: {}", email);
            throw new AuthenticationFailedException("User not found. Please sign up.");
        }

        String jwtToken = jwtUtils.generateToken(user.getUsername());
        user.setToken(jwtToken);

        logger.info("User logged in via Google: {}", email);
        return user;
    }

}
