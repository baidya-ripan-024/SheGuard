package com.sheguard.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDto {

    private String personName;
    private String phoneNumber;
    private String email;
    private String whatsappNumber;
    private Boolean isPrimary;
}
