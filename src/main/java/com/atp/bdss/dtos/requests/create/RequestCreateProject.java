package com.atp.bdss.dtos.requests.create;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestCreateProject {

    String id;

    @NotBlank(message = "Name must not be blank")
    String name;

    String description;

    MultipartFile thumbnail;

    @NotNull(message = "Status must not be null")
    Short status;

    String address;

    LocalDate startDate;

    LocalDate endDate;

    MultipartFile qrImg;

    @NotNull(message = "Bank number must not be null")
    @Pattern(regexp = "^[0-9]*$", message = "Bank number must contain only digits")
    String bankNumber;

    String bankName;

    String hostBank;

    String investor;

    @NotNull(message = "Expiry date must not be null")
    @Positive(message = "Expiry date must be a positive integer")
    Integer expiryDate;

    @NotNull(message = "Deposit must not be null")
    @Pattern(regexp = "\\d+", message = "Deposit must be a valid number")
    String defaultDeposit;

    @NotNull(message = "Investor phone must not be null")
    @Pattern(regexp = "^[0-9]*$", message = "Investor phone must contain only digits")
    @Size(min = 10, max = 10, message = "Investor phone must have exactly 10 digits")
    String investorPhone;

    @NotNull(message = "Project type must not be null")
    String projectType;

    @NotNull(message = "District ID must not be null")
    int districtId;
}
