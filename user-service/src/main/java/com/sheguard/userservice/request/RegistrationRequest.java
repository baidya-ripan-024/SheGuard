package com.sheguard.userservice.request;

import com.sheguard.userservice.enums.USER_ROLE;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    @NotBlank(message = "Full Name is required")
    private String fullName;

    @Email(message = "Invalid Email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 5, message = "Password must be at-least 5 characters")
    private String password;

    private USER_ROLE role;

    /**
     * User can add the contacts later as much as she/he wants to add.
     * he/ she can add address later registration.
     */
}
