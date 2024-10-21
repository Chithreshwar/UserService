package controller;

import dto.SignUpDto;
import entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUpUser(SignUpDto signUpDto){
        try{
            User user = userService.signUp(signUpDto.getName(),signUpDto.getPassword(),signUpDto.getEmail());
            return new ResponseEntity<>(user,HttpStatusCode.valueOf(200));
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
    }
}
