package project.demoChat.controller;


import org.springframework.web.bind.annotation.*;

import project.demoChat.model.User;
import project.demoChat.service.UserService;
import project.demoChat.DTO.RegistrationDTO;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody RegistrationDTO request) {
        return userService.registerUser(request);
    }
}