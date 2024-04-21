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
public class ProvinceDTO {

    int id;

    String name;

    List<DistrictDTO> districts;

    public ProvinceDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
