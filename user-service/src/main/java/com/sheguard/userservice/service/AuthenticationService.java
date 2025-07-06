package com.sheguard.userservice.service;

import com.sheguard.userservice.request.LoginRequest;
import com.sheguard.userservice.request.RegistrationRequest;
import com.sheguard.userservice.response.LoginResponse;
import com.sheguard.userservice.response.RegistrationResponse;

public interface AuthenticationService {

    RegistrationResponse signup(RegistrationRequest registrationRequest) throws Exception;

    LoginResponse login(LoginRequest loginRequest) throws Exception;
}
