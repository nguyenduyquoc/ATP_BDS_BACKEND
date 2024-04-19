package com.atp.bdss.controllers;

import com.atp.bdss.dtos.AreaCreate;
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

    // Hien thi list phan khu theo id cua project
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDataWithPagination allAreas(
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

        return areaService.allAreaOfProject(request);
    }

    // Tao mot phan khu trong mot lan tao
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData createArea(@Valid @RequestBody AreaCreate request) {
        return  areaService.createAreaForProject(request);
    }

    // Chinh sua phan khu trong du an
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData updateArea(@Valid @RequestBody AreaCreate request) {
        return  areaService.updateAreaForProject(request);
    }

    // Tao nhieu phan khu trong mot lan tao
    @PostMapping(value = "/add_multi_area", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData createMultiArea(@PathVariable String id, @Valid @RequestBody RequestCreateMultiObject<AreaCreate> request) {
        request.setId(id);
        return  areaService.createAreaMultiProject(request);
    }
    // tìm dự án theo id
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData getAreaById(@PathVariable String id){
        return areaService.findAreaById(id);
    }



}
