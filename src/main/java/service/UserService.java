package service;

import entities.User;


public interface UserService {
    public User signUp(String name, String password, String email) throws Exception;
}
