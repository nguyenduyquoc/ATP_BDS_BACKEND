package com.atp.bdss.dtos.requests.pagination;

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

    Short provinceId;

    Short districtId;

    Short status;

    String investor;

    Short projectTypeId;

    BigDecimal priceFrom;

    BigDecimal priceTo;


}
