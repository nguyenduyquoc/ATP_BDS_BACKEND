package com.atp.bdss.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDataWithPagination {

    Integer totalRecords;

    Integer totalPages;

    Integer totalRecordFiltered;

    Integer currentPage;

    Integer currentSize;

    Object data;

}