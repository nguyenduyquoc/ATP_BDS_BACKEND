package com.atp.bdss.services;

import com.atp.bdss.dtos.requests.RequestCreateArea;
import com.atp.bdss.dtos.requests.RequestCreateMultiObject;
import com.atp.bdss.dtos.requests.RequestPaginationArea;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import org.springframework.stereotype.Service;

@Service
public interface IAreaService {

    ResponseDataWithPagination allAreas(RequestPaginationArea request);

    ResponseData createAreaForProject(RequestCreateArea request);

    ResponseData createAreaMultiProject(RequestCreateMultiObject<RequestCreateArea> request);

    ResponseData updateAreaForProject(RequestCreateArea request);

    ResponseData findAreaById(String id);
}
