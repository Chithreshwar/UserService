package com.example.userservice.service;

import com.example.userservice.Repository.TokenRespository;
import com.example.userservice.Repository.UserRepository;
import com.example.userservice.entities.Token;
import com.example.userservice.entities.User;
import com.example.userservice.exceptions.ExpiredTokenException;
import com.example.userservice.exceptions.PasswordMismatchException;
import com.example.userservice.exceptions.TokenNotFounException;
import com.example.userservice.exceptions.UserNotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserRepository userRepository;

    private TokenRespository tokenRespository;

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, TokenRespository tokenRespository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.tokenRespository = tokenRespository;
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

    @Override
    public Token logIn(String email, String password) throws Exception {
        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        User user = optionalUser.orElseThrow(()-> new UserNotFoundException("The user not found!"));

        boolean matches = this.bCryptPasswordEncoder.matches(password, user.getPassword());
        if(matches){
            //issue token
            String value = RandomStringUtils.randomAlphanumeric(128);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE,30);
            Date thirtyDaysLater = calendar.getTime();

            Token token = new Token();
            token.setUser(user);
            token.setValue(value);
            token.setExpiresAt(thirtyDaysLater);
            token.setActive(true);

            return this.tokenRespository.save(token);
        }else {
            throw new PasswordMismatchException("Incorrect password");
        }
    }

    @Override
    public Token validateToken(String tokenValue) throws ExpiredTokenException, TokenNotFounException {
        Optional<Token> optionalToken = this.tokenRespository.findTokenByValue(tokenValue);
        Token token = optionalToken.orElseThrow(() -> new TokenNotFounException("Token not found"));

        Date expiresAt = token.getExpiresAt();
        Date now = new Date();
        // If now is greater than expires at
        if(now.after(expiresAt) || !token.isActive()){
            throw new ExpiredTokenException("The token has expired");
        }
        return null;
    }

    @Override
    public void logout(String tokenValue) throws Exception {
        Optional<Token> optionalToken = this.tokenRespository.findTokenByValue(tokenValue);
        Token token = optionalToken.orElseThrow(() -> new TokenNotFounException("Token not found"));
        if(token.isActive()){
            token.setActive(false);
            this.tokenRespository.save(token);
        }
    }
}
