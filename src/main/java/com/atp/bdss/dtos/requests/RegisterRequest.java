package com.atp.bdss.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {

    @NotBlank(message = "Name is mandatory")
    @Size(max = 225, message = "First name must be lest than 255 characters")
    String name;

    @NotBlank(message = "Phone is mandatory")
    @Size(max = 225, message = "First name must be lest than 255 characters")
    String phone;

    @Email(message = "Email is invalid")
    @Size(max = 225, message = "Email must be lest than 255 characters")
    String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters")
    String password;
}
