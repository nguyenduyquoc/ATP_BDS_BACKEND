package com.atp.bdss.services;

import com.atp.bdss.dtos.requests.RequestCreateLand;
import com.atp.bdss.dtos.requests.RequestCreateMultiObject;
import com.atp.bdss.dtos.requests.RequestPaginationLand;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ILandService {


    void LockLandFromUser(String id);

    ResponseDataWithPagination allLands(RequestPaginationLand request);

    ResponseData createLand(RequestCreateLand request) throws IOException;

    ResponseData createMultiLand(RequestCreateMultiObject<RequestCreateLand> request);

    ResponseData updateLand(RequestCreateLand request);

    ResponseData findLandById(String id);




}
