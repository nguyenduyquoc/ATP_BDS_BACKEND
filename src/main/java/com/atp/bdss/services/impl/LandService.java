package com.atp.bdss.services.impl;

import com.atp.bdss.dtos.LandCreate;
import com.atp.bdss.dtos.requests.RequestCreateMultiObject;
import com.atp.bdss.dtos.requests.RequestPaginationArea;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import com.atp.bdss.services.ILandService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LandService implements ILandService {
    @Override
    public void LockLandFromUser(String id) {

    }

    @Override
    public ResponseDataWithPagination allLands(RequestPaginationArea request) {
        return null;
    }

    @Override
    public ResponseData createLand(LandCreate request) {
        return null;
    }

    @Override
    public ResponseData createLand(RequestCreateMultiObject<LandCreate> request) {
        return null;
    }

    @Override
    public ResponseData updateLand(LandCreate request) {
        return null;
    }

    @Override
    public ResponseData findLandById(String id) {
        return null;
    }
}
