package com.sheguard.userservice.controller;

import com.sheguard.userservice.dto.UserDto;
import com.sheguard.userservice.repository.UserRepository;
import com.sheguard.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/profile")
    public ResponseEntity<UserDto> findUserByEmail(Authentication authentication) throws Exception{
        String email = authentication.getName();
        UserDto userDto = userService.findUserByEmail(email);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(Authentication authentication,
                                             @PathVariable Long id) throws Exception{

        String email = authentication.getName();
        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch((auth) ->
                    auth.getAuthority().equals("ROLE_ADMIN")
                );

        UserDto userDto = userService.findUserByEmail(email);

        // Allow only if, User is deleting his own account, or it's Admin
        if(!isAdmin && !userDto.getEmail().equals(email)){
            return new ResponseEntity<>("You are not authorized to delete this user", HttpStatus.FORBIDDEN);
        }
        userService.deleteUser(id);

        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }


}
