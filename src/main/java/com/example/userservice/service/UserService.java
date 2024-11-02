package com.example.userservice.service;

import com.example.userservice.entities.User;


public interface UserService {
    public User signUp(String name, String password, String email) throws Exception;
}
