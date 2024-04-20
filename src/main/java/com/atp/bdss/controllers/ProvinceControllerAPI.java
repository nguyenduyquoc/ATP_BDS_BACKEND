package com.atp.bdss.controllers;

import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.services.IProvinceDistrictService;
import com.atp.bdss.utils.Constants;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = Constants.REQUEST_MAPPING_PREFIX + Constants.VERSION_API_V1 + "/provinces")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProvinceControllerAPI {

    final IProvinceDistrictService provinceDistrictService;


    //  GET ALL PROVINCES WITH PROJECTS
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData getAllProvincesWithProjects(){

        return provinceDistrictService.getAllProvincesWithProjects();
    }


    //  GET ALL DISTRICTS WITH PROJECTS BELONGING TO A PROVINCE.
    @GetMapping(value = "/{provinceId}/allDistrict", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData getAllDistrictByProvinceId(@PathVariable int provinceId){

        return provinceDistrictService.getAllDistrictWithProjectByProvinceId(provinceId);
    }

}

