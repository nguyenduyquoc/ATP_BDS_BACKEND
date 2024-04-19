package com.atp.bdss.services;

import com.atp.bdss.dtos.AreaCreate;
import com.atp.bdss.dtos.requests.RequestCreateMultiObject;
import com.atp.bdss.dtos.requests.RequestPaginationArea;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import org.springframework.stereotype.Service;

@Service
public interface IAreaService {

    ResponseDataWithPagination allAreaOfProject(RequestPaginationArea request);

    ResponseData createAreaForProject(AreaCreate request);

    ResponseData createAreaMultiProject(RequestCreateMultiObject<AreaCreate> request);

    ResponseData updateAreaForProject(AreaCreate request);

    ResponseData findAreaById(String id);
}
