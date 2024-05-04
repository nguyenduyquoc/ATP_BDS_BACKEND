package com.atp.bdss.dtos.requests.pagination;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.checkerframework.checker.units.qual.A;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestPaginationLandByAreaId {


    String areaId;

    Short price;

    Short status;

    String typeOfApartment;

    String direction;
}
