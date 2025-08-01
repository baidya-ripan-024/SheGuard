package com.sheguard.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HomeController {

    @GetMapping
    public ResponseEntity<String> getMessage() {
        return new ResponseEntity<>(
                "Hello from User Service Home Controller",
                HttpStatus.OK
        );
    }
}
