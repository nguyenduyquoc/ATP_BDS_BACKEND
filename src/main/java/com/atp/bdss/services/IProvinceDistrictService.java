package com.atp.bdss.services;

import com.atp.bdss.dtos.responses.ResponseData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProvinceDistrictService {

    ResponseData getAllProvincesWithProjects();

    ResponseData getAllDistrictWithProject();

    ResponseData getProvinceByDistrictId(int districtId);

    ResponseData getAllDistrictWithProjectByProvinceId(int provinceId);


    ResponseData getAllProvinces();

    ResponseData getAllDistrictByProvinceId(int provinceId);
}
