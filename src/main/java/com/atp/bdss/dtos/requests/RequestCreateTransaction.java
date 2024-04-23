package com.atp.bdss.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestCreateTransaction {

    @NotBlank(message = "User id must not be blank")
    String userId;

    @NotBlank(message = "Land id must not be blank")
    String landId;

}
