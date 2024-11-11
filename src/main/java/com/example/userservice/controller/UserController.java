package com.example.userservice.controller;

import com.example.userservice.dto.LogInDto;
import com.example.userservice.dto.LogoutDto;
import com.example.userservice.dto.SignUpDto;
import com.example.userservice.dto.ValidateTokenDto;
import com.example.userservice.entities.Token;
import com.example.userservice.entities.User;
import com.example.userservice.exceptions.ExpiredTokenException;
import com.example.userservice.exceptions.TokenNotFounException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.userservice.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody SignUpDto signUpDto){
        try{
            User user = userService.signUp(signUpDto.getName(),signUpDto.getPassword(),signUpDto.getEmail());
            return new ResponseEntity<>(user,HttpStatusCode.valueOf(201));
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Token> logIn(@RequestBody LogInDto logInDto){
        try{
            Token token = userService.logIn(logInDto.getEmail(), logInDto.getPassword());
            return new ResponseEntity<>(token,HttpStatusCode.valueOf(200));
        }catch (Exception e){
            return  new ResponseEntity<>(HttpStatusCode.valueOf(404));
        }
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Token>validateToken(@RequestBody ValidateTokenDto validateTokenDto){
        try{
            Token token = this.userService.validateToken(validateTokenDto.getToken());
            return new ResponseEntity<>(token, HttpStatusCode.valueOf(200));
        } catch (ExpiredTokenException ete){
            return new ResponseEntity<>(HttpStatusCode.valueOf(401));
        } catch (TokenNotFounException tne){
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutDto logoutDto){
        try{
            this.userService.logout(logoutDto.getToken());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
