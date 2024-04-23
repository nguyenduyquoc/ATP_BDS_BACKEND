package com.atp.bdss.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestCreateArea {

    String id;

    @NotBlank(message = "Project id must not be blank")
    String projectId;

    @NotBlank(message = "Are name must not be blank")
    String name;

    @NotNull(message = "Expiry date must not be null")
    @Positive(message = "Expiry date must be a positive integer")
    Integer expiryDate;
}
