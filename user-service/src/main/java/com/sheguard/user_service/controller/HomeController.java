package com.sheguard.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class HomeController {

    @GetMapping("/hello")
    public ResponseEntity<String> getMessage() {
        return new ResponseEntity<>("Hello from User Service Home Controller", HttpStatus.OK);
    }
}
