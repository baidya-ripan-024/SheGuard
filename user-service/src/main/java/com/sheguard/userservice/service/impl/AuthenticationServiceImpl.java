package com.sheguard.userservice.service.impl;

import com.sheguard.userservice.config.JwtProvider;
import com.sheguard.userservice.model.User;
import com.sheguard.userservice.repository.UserRepository;
import com.sheguard.userservice.request.LoginRequest;
import com.sheguard.userservice.request.RegistrationRequest;
import com.sheguard.userservice.response.LoginResponse;
import com.sheguard.userservice.response.RegistrationResponse;
import com.sheguard.userservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;

    @Override
    public RegistrationResponse signup(RegistrationRequest request) throws Exception {
        log.info("Creating a new user with email {}", request.getEmail());

        // check whether the user already exist in our system
        if(userRepository.findByEmail(request.getEmail()) != null) {
            log.error("User with email {} already exists", request.getEmail());
            throw new RuntimeException("User with email " + request.getEmail() + " already exists");
        }

        User createdUser = new User();
        createdUser.setFullName(request.getFullName());
        createdUser.setEmail(request.getEmail());
        createdUser.setPassword(passwordEncoder.encode(request.getPassword()));
        createdUser.setRole(request.getRole());

        User savedUser = userRepository.save(createdUser);
        log.info("User with email {} created", request.getEmail());

        // Authenticate with raw password (not encoded one)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // generate the jwt token
        String jwt = jwtProvider.generateJwtToken(authentication);

        return new RegistrationResponse(
                jwt,
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getRole().name()
        );
    }

    @Override
    public LoginResponse login(LoginRequest request) throws Exception {
        String email = request.getUsername();
        String password = request.getPassword();

        log.info("Login attempt for user {}", email);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email, password
                )
        );
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        String jwt = jwtProvider.generateJwtToken(authentication);

        return new LoginResponse(
                jwt,
                "login successful"
        );
    }
}
