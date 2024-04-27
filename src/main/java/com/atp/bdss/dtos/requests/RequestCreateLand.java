package com.atp.bdss.dtos.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0")
    BigDecimal price;

    @NotNull(message = "Deposit must not be null")
    @DecimalMin(value = "0.0", message = "Deposit must be greater than or equal to 0")
    BigDecimal deposit;


    @NotNull(message = "Acreage must not be null")
    @Positive(message = "Acreage must be a positive number")
    long acreage;

    short typeOfApartment;

    short direction;

    @NotBlank(message = "Area Id must not be blank")
    String areaId;
}
