package com.atp.bdss.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestOTP {

    private String userId;

    @NotNull(message = "Investor phone must not be null")
    @Pattern(regexp = "^\\+84[0-9]*$", message = "Investor phone must start with '+84' and contain only digits")
    @Size(min = 12, max = 12, message = "Investor phone must have exactly 13 characters including '+84'")
    private String phoneNumber;

}
