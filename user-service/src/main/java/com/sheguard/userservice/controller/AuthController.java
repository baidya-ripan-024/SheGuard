package com.sheguard.userservice.controller;

import com.sheguard.userservice.request.LoginRequest;
import com.sheguard.userservice.request.RegistrationRequest;
import com.sheguard.userservice.response.LoginResponse;
import com.sheguard.userservice.response.RegistrationResponse;
import com.sheguard.userservice.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<RegistrationResponse> registerUser(
            @Valid @RequestBody RegistrationRequest request) throws Exception {

        log.info("Received signup request for email: {}", request.getEmail());

        RegistrationResponse response = authenticationService.signup(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> loginUser(
            @Valid @RequestBody LoginRequest request) throws Exception {

        log.info("Received login request for email: {}", request.getUsername());

        LoginResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }
}
