package com.atp.bdss.services.impl;

import com.atp.bdss.dtos.*;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.entities.District;
import com.atp.bdss.entities.Province;
import com.atp.bdss.exceptions.CustomException;
import com.atp.bdss.repositories.DistrictRepositoryJPA;
import com.atp.bdss.repositories.ProvinceRepositoryJPA;
import com.atp.bdss.services.IProvinceDistrictService;
import com.atp.bdss.utils.ErrorsApp;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Transactional
public class ProvinceDistrictService implements IProvinceDistrictService {

    final ProvinceRepositoryJPA provinceRepository;
    final DistrictRepositoryJPA districtRepository;
    final ModelMapper modelMapper;
    @Override
    public ResponseData getAllProvincesWithProjects() {
        List<Province> provinces = provinceRepository.getAllProvinces();
        if(provinces.isEmpty())
            throw new CustomException(ErrorsApp.RECORD_NOT_FOUND);

        List<ProvinceDTO> data = provinces.stream()
                .map(province -> modelMapper.map(province, ProvinceDTO.class)
        ).toList();


        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .data(data)
                .build();
    }

    @Override
    public ResponseData getAllDistrictWithProject() {
        List<District> districts = districtRepository.getAllDistrict();

        if(districts.isEmpty())
            throw new CustomException(ErrorsApp.RECORD_NOT_FOUND);

        List<DistrictDTO> data = districts.stream()
                .map(district -> modelMapper.map(district, DistrictDTO.class)
                ).toList();


        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .data(data)
                .build();
    }

    @Override
    public ResponseData getProvinceByDistrictId(int districtId) {
        Optional<District> district = districtRepository.findById(districtId);
        if (district.isEmpty())
            throw new CustomException(ErrorsApp.RECORD_NOT_FOUND);
        Province province = district.get().getProvince();
        ProvinceDTO data = ProvinceDTO.builder()
                .id(province.getId())
                .name(province.getName())
                .build();

        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .data(data)
                .build();
    }


    @Override
    public ResponseData getAllDistrictWithProjectByProvinceId(int provinceId) {

        List<District> districts = districtRepository.getAllDistrictByProvince(provinceId);

        if(districts.isEmpty())
            throw new CustomException(ErrorsApp.RECORD_NOT_FOUND);

        List<DistrictDTO> data = districts.stream()
                .map(district -> modelMapper.map(district, DistrictDTO.class)
                ).toList();

        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .data(data)
                .build();
    }
}
