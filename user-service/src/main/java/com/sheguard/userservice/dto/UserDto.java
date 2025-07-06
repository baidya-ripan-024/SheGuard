package com.sheguard.userservice.dto;

import com.sheguard.userservice.enums.USER_ROLE;
import com.sheguard.userservice.model.Address;
import com.sheguard.userservice.model.Contact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String fullName;
    private String email;
    private USER_ROLE role;
    private Address address;
    private List<Contact> contacts;

}
