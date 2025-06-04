package com.carrental.CarService.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.carrental.CarService.model.User;

public class UserPrinciple implements UserDetails {

    private static final Logger logger = LoggerFactory.getLogger(UserPrinciple.class);

    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrinciple(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();

        List<GrantedAuthority> auths = new ArrayList<>();

        // Assuming user.getUserRole() returns a comma-separated string like "ROLE_USER,ROLE_ADMIN"
        String[] roles = user.getUserRole().split(",");
        for (String role : roles) {
            role = role.trim();
            auths.add(new SimpleGrantedAuthority(role));
            logger.debug("Assigned role '{}' to user '{}'", role, username);
        }

        this.authorities = auths;

        logger.info("UserPrinciple created for user: {}", username);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
