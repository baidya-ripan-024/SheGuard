package com.sheguard.userservice.service.impl;

import com.sheguard.userservice.dto.UserDto;
import com.sheguard.userservice.model.User;
import com.sheguard.userservice.repository.UserRepository;
import com.sheguard.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);

        if(user == null) {
            log.error("User with email {} not found", email);
            throw new BadCredentialsException("User with email "+email+ " not found");
        }
        UserDto userDto = mapUserToUserDto(user);

        log.info("User with email {} found", email);
        return userDto;
    }

    @Override
    public List<UserDto> findAllUser() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(this::mapUserToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
           log.error("User with id {} not found", id);
           return new BadCredentialsException("User with id "+id+ " not found");
        });

        userRepository.delete(user);
        log.info("User with id {} deleted", id);
    }

    /**
     * This method will map User object to UserDto object
     */
    private UserDto mapUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFullName(user.getFullName());
        userDto.setEmail(user.getEmail());
        userDto.setAddress(user.getAddress());
        userDto.setContacts(user.getContacts());
        userDto.setRole(user.getRole());

        return userDto;
    }
}
