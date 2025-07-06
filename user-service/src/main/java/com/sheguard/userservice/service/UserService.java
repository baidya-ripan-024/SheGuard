package com.sheguard.userservice.service;

import com.sheguard.userservice.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto findUserByEmail(String email) throws Exception;

    // this method would be accessible by admin only
    List<UserDto> findAllUser();

    void deleteUser(Long id) throws Exception;
}
