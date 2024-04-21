package com.atp.bdss.services.impl;

import com.atp.bdss.dtos.requests.RequestCreateLand;
import com.atp.bdss.dtos.requests.RequestCreateMultiObject;
import com.atp.bdss.dtos.requests.RequestPaginationLand;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import com.atp.bdss.entities.Area;
import com.atp.bdss.entities.Land;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


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
        return null;
    }

    @Override
    public ResponseData createLand(RequestCreateLand request) throws IOException {
        // kiem tra area co ton tai hay k
        Optional<Area> area = areaRepository.findById(request.getAreaId());
        if (area.isEmpty())
            throw new CustomException(ErrorsApp.RECORD_NOT_FOUND);

        // check xem name truyen vao da trung voi name thuoc phan khu nay hay chua
        List<Land> landList = area.get().getLands();

        boolean nameExistsArea = landList.stream()
                .anyMatch(land -> land.getName().equalsIgnoreCase(request.getName()));

        if (nameExistsArea) {
            throw new CustomException(ErrorsApp.DUPLICATE_AREA_NAME);
        }

        String thumbnail = uploadImage(request.getThumbnail(), cloudinary);

        Land land = modelMapper.map(request, Land.class);
        land.setThumbnail(thumbnail);
        land.setArea(area.get());
        landRepository.save(land);

        return ResponseData
                .builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .build();
    }

    @Override
    public ResponseData updateLand(RequestCreateLand request) {
        return null;
    }

    @Override
    public ResponseData findLandById(String id) {
        return null;
    }

    @Override
    public ResponseData createMultiLand(RequestCreateMultiObject<RequestCreateLand> request) {
        return null;
    }

    private static String uploadImage(MultipartFile image, CloudinaryService cloudinaryService) throws IOException {
        String thumbnail = null;
        if(image != null && !image.isEmpty())
            thumbnail = cloudinaryService.upload(image);
        return thumbnail;
    }
}
