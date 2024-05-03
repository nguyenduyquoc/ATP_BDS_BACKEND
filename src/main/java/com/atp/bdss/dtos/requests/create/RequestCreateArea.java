package com.atp.bdss.dtos.requests.create;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestCreateArea {

    String id;

    @NotBlank(message = "Project id must not be blank")
    String projectId;

    @NotBlank(message = "Are name must not be blank")
    String name;

}
