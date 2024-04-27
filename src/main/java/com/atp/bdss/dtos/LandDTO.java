package com.atp.bdss.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LandDTO {

    String id;

    String name;

    String description;

    String thumbnail;

    Short status;

    String address;

    BigDecimal price;

    BigDecimal deposit;

    Long acreage;

    Short typeOfApartment;

    Short direction;

    AreaDTO areaDTO;


}
