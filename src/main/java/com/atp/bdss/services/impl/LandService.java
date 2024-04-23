package com.atp.bdss.services.impl;

import com.atp.bdss.dtos.*;
import com.atp.bdss.dtos.requests.RequestCreateLand;
import com.atp.bdss.dtos.requests.RequestCreateMultiObject;
import com.atp.bdss.dtos.requests.RequestPaginationLand;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import com.atp.bdss.entities.Area;
import com.atp.bdss.entities.Land;
import com.atp.bdss.entities.Project;
import com.atp.bdss.exceptions.CustomException;
import com.atp.bdss.repositories.AreaRepositoryJPA;
import com.atp.bdss.repositories.LandRepositoryJPA;
import com.atp.bdss.services.ILandService;
import com.atp.bdss.services.customService.CloudinaryService;
import com.atp.bdss.utils.ErrorsApp;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static com.atp.bdss.utils.UploadImage.uploadImage;


@Component
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LandService implements ILandService {

    final LandRepositoryJPA landRepository;
    final AreaRepositoryJPA areaRepository;
    final ModelMapper modelMapper;
    final CloudinaryService cloudinary;
    @Override
    public void LockLandFromUser(String id) {

    }

    @Override
    public ResponseDataWithPagination allLands(RequestPaginationLand request) {
        Pageable pageable = PageRequest.of(request.getPageIndex() != null ? request.getPageIndex() : 0,
                Math.max(request.getPageSize() != null ? request.getPageSize().intValue() : 8, 1));

        if (request.getSearchName() != null)
            request.setSearchName(request.getSearchName().replace("%", "\\%").replace("_", "\\_").trim());

        Page<Land> data = landRepository.getLandPagination(request, pageable);

        if(data.isEmpty()){
            return (ResponseDataWithPagination) Page.empty();
        }

        Page<LandDTO> landDTOS = data.map(land -> {
            LandDTO landDTO = modelMapper.map(land, LandDTO.class);
            if(land.getArea() != null) {
                AreaDTO areaDTO = AreaDTO.builder()
                        .id(land.getArea().getId())
                        .name(land.getArea().getName())
                        .expiryDate(land.getArea().getExpiryDate())
                        .projectId(land.getArea().getProject().getId())
                        .projectName(land.getArea().getProject().getName())
                        .build();
                landDTO.setAreaDTO(areaDTO);
            }
            return landDTO;
        });

        return ResponseDataWithPagination.builder()
                .currentPage(data.getNumber())
                .currentSize(data.getSize())
                .totalRecords((int) data.getTotalElements())
                .totalPages(data.getTotalPages())
                .totalRecordFiltered(data.getNumberOfElements())
                .data(landDTOS.getContent())
                .build();
    }

    @Override
    public ResponseData createLand(RequestCreateLand request) throws IOException {
        // kiem tra area co ton tai hay k
        Area area = areaRepository.findById(request.getAreaId())
                .orElseThrow(() -> new CustomException(ErrorsApp.AREA_NOT_FOUND));

        // check xem name truyen vao da trung voi name cua cac land thuoc phan khu nay hay chua
        if (landRepository.existsByNameIgnoreCaseAndAreaId(request.getName(), request.getAreaId()))
            throw new CustomException(ErrorsApp.DUPLICATE_LAND_NAME);

        String thumbnail = uploadImage(request.getThumbnail(), cloudinary);
        Land land = modelMapper.map(request, Land.class);
        land.setThumbnail(thumbnail);
        land.setArea(area);
        landRepository.save(land);

        return ResponseData
                .builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .build();
    }

    @Override
    public ResponseData updateLand(RequestCreateLand request) throws IOException {
        // kiem tra land co ton tai hay k
        Land optionalLand = landRepository.findById(request.getId())
                .orElseThrow(() -> new CustomException(ErrorsApp.LAND_NOT_FOUND));

        // kiem tra area co ton tai hay k
        Area area = areaRepository.findById(request.getAreaId())
                .orElseThrow(() -> new CustomException(ErrorsApp.AREA_NOT_FOUND));

        // check xem name truyen vao da trung voi name cua cac land thuoc phan khu nay hay chua
        if (!optionalLand.getName().equalsIgnoreCase(request.getName())) {
            if (landRepository.existsByNameIgnoreCaseAndAreaId(request.getName(), area.getId())) {
                throw new CustomException(ErrorsApp.DUPLICATE_LAND_NAME);
            }
            optionalLand.setName(request.getName());
        }

        // setThumbnail
        if(request.getThumbnail() != null && !request.getThumbnail().isEmpty()){
            String thumbnail = uploadImage(request.getThumbnail(), cloudinary);
            optionalLand.setThumbnail(thumbnail);
        }
        optionalLand.setDescription(request.getDescription());
        optionalLand.setAddress(request.getAddress());
        optionalLand.setStatus(request.getStatus());
        optionalLand.setPrice(request.getPrice());
        optionalLand.setDeposit(request.getDeposit());
        optionalLand.setAcreage(request.getAcreage());
        landRepository.save(optionalLand);

        return ResponseData
                .builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .build();
    }

    @Override
    public ResponseData findLandById(String id) {
        Land land = landRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorsApp.LAND_NOT_FOUND));
        LandDTO landDTO = modelMapper.map(land, LandDTO.class);

        Area area = land.getArea();
        if(area != null) {

            AreaDTO areaDTO = AreaDTO.builder()
                    .id(area.getId())
                    .name(area.getName())
                    .expiryDate(area.getExpiryDate())
                    .projectId(area.getProject().getId())
                    .projectName(area.getProject().getName())
                    .build();
            landDTO.setAreaDTO(areaDTO);
        }

        return ResponseData
                .builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .data(landDTO)
                .build();
    }

    @Override
    public ResponseData createMultiLand(RequestCreateMultiObject<RequestCreateLand> request) {
        return null;
    }



}
