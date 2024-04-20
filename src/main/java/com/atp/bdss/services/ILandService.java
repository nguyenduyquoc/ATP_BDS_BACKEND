package com.atp.bdss.services;

import com.atp.bdss.dtos.LandCreate;
import com.atp.bdss.dtos.requests.RequestCreateMultiObject;
import com.atp.bdss.dtos.requests.RequestPaginationArea;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import org.springframework.stereotype.Service;

@Service
public interface ILandService {


    void LockLandFromUser(String id);

    ResponseDataWithPagination allLands(RequestPaginationArea request);

    ResponseData createLand(LandCreate request);

    ResponseData createLand(RequestCreateMultiObject<LandCreate> request);

    ResponseData updateLand(LandCreate request);

    ResponseData findLandById(String id);




}
