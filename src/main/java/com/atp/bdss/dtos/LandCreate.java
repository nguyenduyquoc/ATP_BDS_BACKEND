package com.atp.bdss.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LandCreate {
    String name;

    String description;

    String thumbnail;

    short address;

    BigDecimal price;

    BigDecimal deposit;

    long acreage;

    short areaId;
}
