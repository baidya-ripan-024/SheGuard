package com.sheguard.userservice.service;

import com.sheguard.userservice.dto.AddressDto;

import java.util.List;

public interface AddressService {

    AddressDto addAddress(String email, AddressDto dto);

    AddressDto updateAddress(Long addressId, String email, AddressDto dto);

    // only Admin can see all the addresses.
    List<AddressDto> getAllUserAddresses();

    AddressDto getAddressForUser(String email);

    void deleteAddress(Long addressId, String email);
}
