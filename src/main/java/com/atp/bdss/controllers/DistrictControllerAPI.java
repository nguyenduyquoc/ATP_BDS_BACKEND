package com.atp.bdss.controllers;

import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.services.IProvinceDistrictService;
import com.atp.bdss.utils.Constants;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Constants.REQUEST_MAPPING_PREFIX + Constants.VERSION_API_V1 + "/districts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DistrictControllerAPI {

    final IProvinceDistrictService provinceDistrictService;

    //  GET ALL DISTRICTS WITH PROJECTS
    @GetMapping(value = "/withProject", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData addDistrictWithProject(){

        return provinceDistrictService.getAllDistrictWithProject();
    }

    //  GET PROVINCE BY DISTRICT ID
    @GetMapping(value = "/{districtId}/getProvince", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData getProvinceByDistrictId(@PathVariable int districtId){

        return provinceDistrictService.getProvinceByDistrictId(districtId);
    }




}
