package com.atp.bdss.services;

import com.atp.bdss.dtos.requests.create.RequestCreateArea;
import com.atp.bdss.dtos.requests.create.RequestCreateMultiObject;
import com.atp.bdss.dtos.requests.pagination.RequestPaginationArea;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import org.springframework.stereotype.Service;

@Service
public interface IAreaService {

    ResponseDataWithPagination allAreasWithPagination(RequestPaginationArea request);

    ResponseData createAreaForProject(RequestCreateArea request);

    ResponseData createAreaMultiProject(RequestCreateMultiObject<RequestCreateArea> request);

    ResponseData updateAreaForProject(RequestCreateArea request);

    ResponseData findAreaById(String id);

    ResponseData delete(String id);

    ResponseData allAreasNoPagination();
}
