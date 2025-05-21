package com.flight.payment.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/success")
    public String success() {
        return "successs";
    }

}
