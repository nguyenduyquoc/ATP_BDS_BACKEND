package com.atp.bdss.dtos.requests.pagination;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestPaginationLandByAreaId {


    String areaId;

    Short price;

    Short status;

    String typeOfApartment;

    String direction;
}
