package com.atp.bdss.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    String bankNumber;

    String bankName;

    String hostBank;

    String investor;

    String investorPhone;

    @NotNull(message = "Project type ID must not be null")
    Short projectTypeId;

    @NotNull(message = "District ID must not be null")
    int districtId;
}
