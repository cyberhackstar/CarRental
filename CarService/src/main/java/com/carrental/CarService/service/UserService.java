package com.carrental.CarService.service;

import com.carrental.CarService.model.User;

public interface UserService {
    User createUser(User user);

    User loginUser(User user);

    User loginWithGoogle(String idToken);

    User signupWithGoogle(String idToken);

}
