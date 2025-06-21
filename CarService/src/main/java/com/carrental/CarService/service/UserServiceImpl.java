package com.carrental.CarService.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.carrental.CarService.config.JwtUtils;
import com.carrental.CarService.exception.AuthenticationFailedException;
import com.carrental.CarService.exception.UserAlreadyExistsException;
import com.carrental.CarService.model.User;
import com.carrental.CarService.repository.UserRepo;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
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

}
