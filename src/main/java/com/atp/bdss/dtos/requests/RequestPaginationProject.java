package com.atp.bdss.dtos.requests;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestPaginationProject {

    Short pageIndex;

    Short pageSize;

    String nameProject;

    String provinceId;

    String districtId;

    String status;

    String investor;

    String projectTypeId;

    BigDecimal priceFrom;

    BigDecimal priceTo;


}
