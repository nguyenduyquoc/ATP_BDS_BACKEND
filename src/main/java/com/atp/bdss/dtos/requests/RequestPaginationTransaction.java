package com.atp.bdss.dtos.requests;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestPaginationTransaction {

    Short pageIndex;

    Short pageSize;

    String searchByLandName;

    Short status;

}
