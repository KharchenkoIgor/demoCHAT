package project.demoChat.features.auth;


import org.springframework.web.bind.annotation.*;

import project.demoChat.domain.User;

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