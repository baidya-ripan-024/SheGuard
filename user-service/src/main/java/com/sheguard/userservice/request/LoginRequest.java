package com.sheguard.userservice.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.annotation.Documented;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    // email
    private String username;

    private String password;
}
