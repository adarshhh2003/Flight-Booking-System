package com.security.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class TestController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "Welcome to the Application";
    }

    @GetMapping("/greet")
    public String greet() {
        return "Welcome User";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/userOnly")
    public String userOnly() {
        return "This is for user only";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/adminOnly")
    public String adminOnly() {
        return "This is for admin only";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/both")
    public String both() {
        return "This is for admin and user both";
    }

}
