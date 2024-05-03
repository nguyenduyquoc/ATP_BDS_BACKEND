package com.atp.bdss.dtos.requests.create;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestCreateLand {

    String id;

    @NotBlank(message = "Land name must not be blank")
    String name;

    @NotBlank(message = "Description must not be blank")
    String description;

    MultipartFile thumbnail;

    @NotBlank(message = "address must not be blank")
    String address;

    @NotNull(message = "Status must not be null")
    short status;


    @Pattern(regexp = "\\d+", message = "Price must be a valid number")
    String price;

    @Pattern(regexp = "\\d+", message = "Deposit must be a valid number")
    String deposit;


    @NotNull(message = "Acreage must not be null")
    @Positive(message = "Acreage must be a positive number")
    long acreage;

    String typeOfApartment;

    String direction;

    @NotBlank(message = "Area Id must not be blank")
    String areaId;
}
