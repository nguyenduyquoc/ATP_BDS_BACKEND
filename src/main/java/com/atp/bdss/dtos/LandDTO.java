package com.atp.bdss.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


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

    Short status;

    String address;

    String price;

    String deposit;

    Long acreage;

    String typeOfApartment;

    String direction;

    AreaDTO areaDTO;

    List<ImageDTO> images;

}
