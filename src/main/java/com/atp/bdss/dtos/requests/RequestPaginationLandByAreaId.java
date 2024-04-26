package com.atp.bdss.dtos.requests;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestPaginationLandByAreaId {

    //Integer pageIndex;

    //Long pageSize;

    String areaId;

    Short price;

    Short status;

    String description;
}
