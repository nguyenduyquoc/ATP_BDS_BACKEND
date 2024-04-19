package com.atp.bdss.dtos.requests;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestPaginationLand {

    Short pageIndex;

    Short pageSize;

    String projectId;

    String areaId;

    String landName;
}
