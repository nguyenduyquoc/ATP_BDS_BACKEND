package com.atp.bdss.services;

import com.atp.bdss.dtos.requests.create.RequestCreateLand;
import com.atp.bdss.dtos.requests.pagination.RequestPaginationLand;
import com.atp.bdss.dtos.requests.pagination.RequestPaginationLandByAreaId;
import com.atp.bdss.dtos.requests.pagination.RequestPaginationLandByProjectId;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ILandService {

    ResponseDataWithPagination allLands(RequestPaginationLand request);

    ResponseData createLand(RequestCreateLand request) throws IOException;

    ResponseData updateLand(RequestCreateLand request) throws IOException;

    ResponseData findLandById(String id);


    ResponseData temporarilyLockOrUnLock(String id, short status);


    ResponseData delete(String id);

    ResponseData filterAllLandsByAreaId(RequestPaginationLandByAreaId request);

    ResponseData getAllTypeOfApartment();

    ResponseData getAllDirection();

    ResponseData filterAllLandsByProjectId(RequestPaginationLandByProjectId request);
}
