package com.security.controllers;

import com.security.entity.LoginRequest;
import com.security.entity.Users;
import com.security.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/security")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> addUser(@Valid @RequestBody Users users) {
        Optional<Users> existingUser = userService.checkExistingUser(users.getEmail());

        if(existingUser.isPresent()) {
            return ResponseEntity.badRequest()
                    .body("An account with user already exists.");
        }

        return userService.addUser(users);
    }

    @PutMapping("/profile/update/{email}")
    public ResponseEntity<String> updateUser(@Valid @PathVariable String email, @Valid @RequestBody Users users) {
        return userService.updateUser(email, users);
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.verify(loginRequest);
    }

}
