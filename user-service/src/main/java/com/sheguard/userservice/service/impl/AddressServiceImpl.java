package com.sheguard.userservice.service.impl;

import com.sheguard.userservice.dto.AddressDto;
import com.sheguard.userservice.model.Address;
import com.sheguard.userservice.model.User;
import com.sheguard.userservice.repository.AddressRepository;
import com.sheguard.userservice.repository.UserRepository;
import com.sheguard.userservice.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Override
    public AddressDto addAddress(String email, AddressDto dto) {
        log.info("Adding Address for user with email {}", email);

        User user = userRepository.findByEmail(email);
        if(user == null) {
            log.error("User with email {} not found", email);
            throw new BadCredentialsException("User with email " + email + " not found");
        }

        Address createdAddress = new Address();
        createdAddress.setLine(dto.getLine());
        createdAddress.setCity(dto.getCity());
        createdAddress.setState(dto.getState());
        createdAddress.setPinCode(dto.getPinCode());
        createdAddress.setUser(user);

        Address savedAddress = addressRepository.save(createdAddress);

        // If user has a single address field, update it
        if (user.getAddress() == null) {
            user.setAddress(savedAddress);
            userRepository.save(user);
        }

        return mapAddressToAddressDto(savedAddress);
    }

    @Override
    public AddressDto updateAddress(Long addressId, String email, AddressDto dto) {
        log.info("Updating address with id {} for user {}", addressId, email);

        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.error("User not found with email {}", email);
            throw new BadCredentialsException("User not found with email: " + email);
        }

        Address address = addressRepository.findById(addressId).orElseThrow(() -> {
            log.error("Address not found with id {}", addressId);
            return new BadCredentialsException("Address not found with id: " + addressId);
        });

        // Check that the address belongs to the user
        if (!address.getUser().getId().equals(user.getId())) {
            log.error("Unauthorized address update attempt by user {}", email);
            throw new AccessDeniedException("You do not have permission to update this address");
        }

        // Update only provided fields
        if (dto.getLine() != null && !dto.getLine().isEmpty()) address.setLine(dto.getLine());
        if (dto.getCity() != null && !dto.getCity().isEmpty()) address.setCity(dto.getCity());
        if (dto.getState() != null && !dto.getState().isEmpty()) address.setState(dto.getState());
        if (dto.getPinCode() != null && !dto.getPinCode().isEmpty()) address.setPinCode(dto.getPinCode());

        Address saved = addressRepository.save(address);
        return mapAddressToAddressDto(saved);
    }


    /**
     * This method is only accessible for the ADMIN
     * @return list of addresses
     */
    @Override
    public List<AddressDto> getAllUserAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream()
                .map(this::mapAddressToAddressDto)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDto getAddressForUser(String email){
        log.info("Getting address for user with email {}", email);

        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.error("User not found with email {}", email);
            throw new BadCredentialsException("User not found with email: " + email);
        }
        Address address = addressRepository.findById(user.getAddress().getId()).orElseThrow(() -> {
            log.error("Address not found with id {}", user.getAddress().getId());
            return new BadCredentialsException("Address not found with id: " + user.getAddress().getId());
        });

        return mapAddressToAddressDto(address);
    }

    @Override
    public void deleteAddress(Long id, String email) {
        log.info("Deleting address with id {} for user {}", id, email);

        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.error("User not found with email {}", email);
            throw new BadCredentialsException("User not found with email: " + email);
        }

        Address address = addressRepository.findById(id).orElseThrow(() -> {
            log.error("Address not found with id {}", id);
            return new BadCredentialsException("Address not found with id: " + id);
        });

        // Check that the address belongs to the user
        if (!address.getUser().getId().equals(user.getId())) {
            log.error("Unauthorized address delete attempt by user {}", email);
            throw new AccessDeniedException("You do not have permission to delete this address");
        }

        // Break the one-to-one relationship to trigger orphanRemoval
        // Address will be automatically removed by hibernate
        user.setAddress(null);
        userRepository.save(user);

        log.info("Address for id {} deleted", id);
    }

    private AddressDto mapAddressToAddressDto(Address address) {
        log.info("Mapping Address to AddressDto.");
        AddressDto addressDto = new AddressDto();
        addressDto.setId(address.getId());
        addressDto.setLine(address.getLine());
        addressDto.setCity(address.getCity());
        addressDto.setState(address.getState());
        addressDto.setPinCode(address.getPinCode());

        return addressDto;
    }
}
