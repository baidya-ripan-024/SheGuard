package com.sheguard.userservice.controller;

import com.sheguard.userservice.dto.AddressDto;
import com.sheguard.userservice.dto.UserDto;
import com.sheguard.userservice.service.AddressService;
import com.sheguard.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final AddressService addressService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAllUser();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDto>> getAllUserAddresses() {
        List<AddressDto> addresses = addressService.getAllUserAddresses();

        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }
}
