package com.atp.bdss.controllers;

import com.atp.bdss.dtos.requests.RequestCreateLand;
import com.atp.bdss.dtos.requests.RequestPaginationLand;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import com.atp.bdss.services.ILandService;
import com.atp.bdss.utils.Constants;
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

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDataWithPagination allLandLots(@RequestParam("pageIndex") int pageIndex,
                                                  @RequestParam("pageSize") long pageSize,
                                                  @RequestParam("searchName") String searchName,
                                                  @RequestParam("projectId") String projectId,
                                                  @RequestParam("areaId") String areaId){

        RequestPaginationLand request = new RequestPaginationLand();
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setSearchName(searchName);
        request.setProjectId(projectId);
        request.setAreaId(areaId);
        return landService.allLands(request);
    }

    @PostMapping(value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData createLandLot(@RequestParam("name") String name,
                                      @RequestParam("description") String description,
                                      @RequestParam(value = "thumbnail", required = false) MultipartFile image,
                                      @RequestParam("address") String address,
                                      @RequestParam("status") Short status,
                                      @RequestParam("price") BigDecimal price,
                                      @RequestParam("deposit") BigDecimal deposit,
                                      @RequestParam("acreage") Short acreage,
                                      @RequestParam("areaId") String areaId) throws IOException {

        RequestCreateLand request = new RequestCreateLand();

        request.setName(name);
        request.setDescription(description);
        request.setThumbnail(image);
        request.setAddress(address);
        request.setStatus(status);
        request.setPrice(price);
        request.setDeposit(deposit);
        request.setAcreage(acreage);
        request.setAreaId(areaId);

        return landService.createLand(request);
    }

    @PutMapping(value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData updateLandLot(@RequestParam("id") String id,
                                      @RequestParam("name") String name,
                                      @RequestParam("description") String description,
                                      @RequestParam("thumbnail") MultipartFile image,
                                      @RequestParam("address") String address,
                                      @RequestParam("status") Short status,
                                      @RequestParam("price") BigDecimal price,
                                      @RequestParam("deposit") BigDecimal deposit,
                                      @RequestParam("acreage") Short acreage,
                                      @RequestParam("areaId") String areaId) {

        RequestCreateLand request = new RequestCreateLand();
        request.setId(id);
        request.setName(name);
        request.setDescription(description);
        request.setThumbnail(image);
        request.setAddress(address);
        request.setStatus(status);
        request.setPrice(price);
        request.setDeposit(deposit);
        request.setAcreage(acreage);
        request.setAreaId(areaId);
        return landService.updateLand(request);
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData getAreaById(@PathVariable String id){

        return landService.findLandById(id);

    }


}
