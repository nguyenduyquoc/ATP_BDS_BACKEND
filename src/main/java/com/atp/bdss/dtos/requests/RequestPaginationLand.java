package com.atp.bdss.dtos.requests;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestPaginationLand {

    Integer pageIndex;

    Long pageSize;

    String searchName;

    String projectId;

    String areaId;
}
