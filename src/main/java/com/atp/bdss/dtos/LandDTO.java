package com.atp.bdss.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LandDTO {

    String id;

    String name;

    String description;

    String thumbnail;

    short status;

    String address;

    BigDecimal price;

    BigDecimal deposit;

    long acreage;

}
