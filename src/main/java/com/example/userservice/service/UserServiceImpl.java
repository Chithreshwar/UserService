package com.example.userservice.service;

import com.example.userservice.Repository.UserRepository;
import com.example.userservice.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }


    @Override
    public User signUp(String name, String password, String email) throws Exception {
        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if(optionalUser.isPresent()){
            throw new Exception("User already present");
        }
        String encodedPassWord = this.bCryptPasswordEncoder.encode(password);
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(encodedPassWord);
        return this.userRepository.save(user);
    }
}
