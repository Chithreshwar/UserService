package com.example.userservice.service;

import com.example.userservice.entities.Token;
import com.example.userservice.entities.User;
import com.example.userservice.exceptions.ExpiredTokenException;
import com.example.userservice.exceptions.TokenNotFounException;


public interface UserService {
    public User signUp(String name, String password, String email) throws Exception;

    public Token logIn(String email, String password) throws Exception;

    public Token validateToken(String tokenValue) throws ExpiredTokenException, TokenNotFounException;
}
