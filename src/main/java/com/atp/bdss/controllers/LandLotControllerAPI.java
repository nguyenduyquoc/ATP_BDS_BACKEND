package com.atp.bdss.controllers;

import com.atp.bdss.dtos.requests.RequestCreateLand;
import com.atp.bdss.dtos.requests.RequestPaginationLand;
import com.atp.bdss.dtos.requests.RequestPaginationLandByAreaId;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import com.atp.bdss.services.ILandService;
import com.atp.bdss.utils.Constants;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping(value = Constants.REQUEST_MAPPING_PREFIX + Constants.VERSION_API_V1 + "/lands")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LandLotControllerAPI {

    final ILandService landService;


    // lay danh sach cac lo dat co phan trang
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDataWithPagination allLandLots(@RequestParam(name = "pageIndex", defaultValue = "0") int pageIndex,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") long pageSize,
                                                  @RequestParam(name = "searchName", required = false) String searchName,
                                                  @RequestParam(name = "projectId", required = false) String projectId,
                                                  @RequestParam(name = "areaId", required = false) String areaId){

        RequestPaginationLand request = new RequestPaginationLand();
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setSearchName(searchName);
        request.setProjectId(projectId);
        request.setAreaId(areaId);
        return landService.allLands(request);
    }

    // tao moi lo dat
    @PostMapping(value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData createLandLot(@ModelAttribute @Valid RequestCreateLand request) throws IOException {

        return landService.createLand(request);
    }

    // cap nhat lo dat
    @PutMapping(value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData updateLandLot(@ModelAttribute @Valid RequestCreateLand request) throws IOException {

        return landService.updateLand(request);
    }


    // tim lo dat bang id
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData getLandById(@PathVariable String id){

        return landService.findLandById(id);

    }

    // khoa hoac mo khoa lo dat
    @PutMapping(value = "/temporarilyLockOrUnLock", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData temporarilyLockOrUnLock(
            @RequestParam("id") String id,
            @RequestParam("status") short status)
    {

        return landService.temporarilyLockOrUnLock(id, status);
    }

    // xoa khu vuc
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData deleteLand(@PathVariable String id)
    {
        return landService.delete(id);
    }


    // lay danh sach cac lo dat co phan trang
    @GetMapping(value = "/allLandByAreaId", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData allLandLotsByAreaId(
            @RequestParam(name = "areaId", required = false) String areaId,
            @RequestParam(name = "price", required = false) Short price,
            @RequestParam(name = "status", required = false) Short status,
            @RequestParam(name = "description", required = false) String description
    ){

        RequestPaginationLandByAreaId request = new RequestPaginationLandByAreaId();
        /*request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);*/
        if (price != null)
            request.setPrice(price);
        if (status != null)
            request.setStatus(status);

        if (description.equals(",")) {
            request.setDescription(null);
        } else request.setDescription(description);
        if (areaId != null)
            request.setAreaId(areaId);

        return landService.allLandsByAreaId(request);
    }



}
