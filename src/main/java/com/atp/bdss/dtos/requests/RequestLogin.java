package com.atp.bdss.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestLogin {

    @Email(message = "Email is invalid")
    @NotEmpty(message = "Email is required")
    String email;

    @NotBlank(message = "Password is required")
    // Min length of password is 6
    @Pattern(regexp = "^.{6,}$", message = "Password must be at least 6 characters")
    String password;

}
