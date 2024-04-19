package com.atp.bdss.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AreaDTO {

    String id;

    String name;

    String projectName;

    List<LandDTO> lands;


    public AreaDTO(String id, String name, String projectName) {
        this.id = id;
        this.name = name;
        this.projectName = projectName;
    }
}
