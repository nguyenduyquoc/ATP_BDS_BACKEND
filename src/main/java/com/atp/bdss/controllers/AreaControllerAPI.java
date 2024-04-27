package com.atp.bdss.controllers;

import com.atp.bdss.dtos.requests.RequestCreateArea;
import com.atp.bdss.dtos.requests.RequestCreateMultiObject;
import com.atp.bdss.dtos.requests.RequestPaginationArea;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import com.atp.bdss.services.IAreaService;
import com.atp.bdss.utils.Constants;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = Constants.REQUEST_MAPPING_PREFIX + Constants.VERSION_API_V1 + "/areas")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AreaControllerAPI {

    final IAreaService areaService;

    // Phan trang area cho admin
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDataWithPagination allAreasWithPagination(
            @RequestParam(name = "pageIndex", defaultValue = "0") Short pageIndex,
            @RequestParam(name = "pageSize", defaultValue = "10") Short pageSize,
            @RequestParam(name = "projectId", required = false) String projectId,
            @RequestParam(name = "areaName", required = false) String areaName
    ){
        RequestPaginationArea request = new RequestPaginationArea();
                request.setPageIndex(pageIndex);
                request.setPageSize(pageSize);
                request.setProjectId(projectId);
                request.setAreaName(areaName);

        return areaService.allAreasWithPagination(request);
    }



    // lay tat ca area khong phan trang
    @GetMapping(value = "/noPagination", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData allAreasNoPagination(){


        return areaService.allAreasNoPagination();
    }

    // Tao mot phan khu trong mot lan tao
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData createArea(@Valid @RequestBody RequestCreateArea request) {

        return  areaService.createAreaForProject(request);
    }

    // Chinh sua phan khu trong du an - hien tai chi cho chinh sua name, chua cho chinh sua project, them vao sau
    // neu phan khu day da co land thi k cho doi phan khu
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData updateArea(@Valid @RequestBody RequestCreateArea request) {

        return areaService.updateAreaForProject(request);
    }


    // Tao nhieu phan khu trong mot lan tao
    @PostMapping(value = "/{id}/addMultiArea", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData createMultiArea(@PathVariable String id, @Valid @RequestBody RequestCreateMultiObject<RequestCreateArea> request) {
        request.setId(id);
        return areaService.createAreaMultiProject(request);
    }


    // tìm phan khu theo id
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData getAreaById(@PathVariable String id){

        return areaService.findAreaById(id);
    }

    // xoa phan khu
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData deleteArea(@PathVariable String id)
    {

        return areaService.delete(id);
    }
}
