package com.atp.bdss.dtos.requests;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestLogin {

    @NotNull(message = "Investor phone must not be null")
    @Pattern(regexp = "^[0-9]*$", message = "Investor phone must contain only digits")
    @Size(min = 10, max = 10, message = "Investor phone must have exactly 10 digits")
    String phone;

    @NotBlank(message = "Password is required")
    // Min length of password is 6
    @Pattern(regexp = "^.{6,}$", message = "Password must be at least 6 characters")
    String password;

}
