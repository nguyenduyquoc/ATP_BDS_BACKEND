package com.atp.bdss.dtos.requests.pagination;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestPaginationUser {

    Short pageIndex;

    Short pageSize;

    String search;

}
