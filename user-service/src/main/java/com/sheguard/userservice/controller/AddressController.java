package com.sheguard.userservice.controller;

import com.sheguard.userservice.dto.AddressDto;
import com.sheguard.userservice.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/address")
public class AddressController {

    private final AddressService addressService;


    @PostMapping
    public ResponseEntity<AddressDto> addAddress(Authentication authentication,
                                                 @RequestBody AddressDto dto) {

        String email = authentication.getName();
        AddressDto address = addressService.addAddress(email, dto);

        return new ResponseEntity<>(address, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long id,
                                                    Authentication authentication,
                                                    @RequestBody AddressDto dto) {

        String email = authentication.getName();
        AddressDto address = addressService.updateAddress(id, email, dto);

        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<AddressDto> getAddressForUser(Authentication authentication) {
        String email = authentication.getName();
        AddressDto address = addressService.getAddressForUser(email);

        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id,
                                                Authentication authentication) {
        String email = authentication.getName();
        addressService.deleteAddress(id, email);

        return new ResponseEntity<>("Address Deleted Successfully." ,HttpStatus.OK);
    }


}
