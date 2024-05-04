package com.atp.bdss.services.impl;

import com.atp.bdss.dtos.*;
import com.atp.bdss.dtos.requests.create.RequestCreateLand;
import com.atp.bdss.dtos.requests.pagination.RequestPaginationLand;
import com.atp.bdss.dtos.requests.pagination.RequestPaginationLandByAreaId;
import com.atp.bdss.dtos.requests.pagination.RequestPaginationLandByProjectId;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import com.atp.bdss.entities.Area;
import com.atp.bdss.entities.Image;
import com.atp.bdss.entities.Land;
import com.atp.bdss.exceptions.CustomException;
import com.atp.bdss.repositories.AreaRepositoryJPA;
import com.atp.bdss.repositories.ImageRepositoryJPA;
import com.atp.bdss.repositories.LandRepositoryJPA;
import com.atp.bdss.services.ILandService;
import com.atp.bdss.services.customService.CloudinaryService;
import com.atp.bdss.utils.Constants;
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

import static com.atp.bdss.utils.CheckerStatus.*;
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
    final ImageRepositoryJPA imageRepository;

    @Override
    public ResponseDataWithPagination allLands(RequestPaginationLand request) {
        Pageable pageable = PageRequest.of(request.getPageIndex() != null ? request.getPageIndex() : 0,
                Math.max(request.getPageSize() != null ? request.getPageSize().intValue() : 10, 1));

        if (request.getSearchName() != null)
            request.setSearchName(request.getSearchName().replace("%", "\\%").replace("_", "\\_").trim());

        Page<Land> data = landRepository.getLandPagination(request, pageable);

        Page<LandDTO> landDTOS = data.map(land -> convertLandToLandDTO(land, modelMapper));

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

        if (!findStatusLand(request.getStatus()))
            throw new CustomException(ErrorsApp.STATUS_LAND_NOT_FOUND);
        String thumbnail = uploadImage(request.getThumbnail(), cloudinary);
        Land land = modelMapper.map(request, Land.class);
        land.setThumbnail(thumbnail);
        land.setArea(area);
        if (request.getDeposit() == null) {
            land.setDeposit(land.getArea().getProject().getDefaultDeposit());
        }
        if (!request.getDirection().isEmpty()) {
            land.setDirection(request.getDirection().replaceAll("[^a-zA-Z0-9+]", ""));
        }
        landRepository.save(land);

        return ResponseData
                .builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .data(land.getId())
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
        }

        if (!findStatusLand(request.getStatus()))
            throw new CustomException(ErrorsApp.STATUS_LAND_NOT_FOUND);

        String thumbnailOld = optionalLand.getThumbnail();
        modelMapper.map(request, optionalLand);

        // setThumbnail
        if(request.getThumbnail() != null && !request.getThumbnail().isEmpty()){
            String thumbnailNew = uploadImage(request.getThumbnail(), cloudinary);
            optionalLand.setThumbnail(thumbnailNew);
        } else {
            optionalLand.setThumbnail(thumbnailOld);
        }

        landRepository.save(optionalLand);

        return ResponseData.builder()
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
    public ResponseData temporarilyLockOrUnLock(String id, short status) {
        Land land = landRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorsApp.LAND_NOT_FOUND));
        if (!findStatusLand(status))
            throw new CustomException(ErrorsApp.STATUS_NOT_FOUND);

        land.setStatus(status);
        landRepository.save(land);
        return ResponseData
                .builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .build();
    }

    @Override
    public ResponseData delete(String id) {
        Land land = landRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorsApp.LAND_NOT_FOUND)
        );

        // kiem tra lo dat ay da ban hoac dang khoa hay k
        if (land.getStatus() == Constants.STATUS_lAND.LOCKING || land.getStatus() == Constants.STATUS_lAND.LOCKED) {
            throw new CustomException(ErrorsApp.CAN_NOT_DELETE_PROJECT);
        }

        landRepository.deleteById(id);
        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .build();
    }

    @Override
    public ResponseData filterAllLandsByAreaId(RequestPaginationLandByAreaId request) {
        List<LandDTO> landList = landRepository.getLandPaginationByAreaID(request)
                .stream().map(land -> convertLandToLandDTO(land, modelMapper)).toList();

        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .data(landList)
                .build();
    }

    @Override
    public ResponseData getAllTypeOfApartment() {
        List<LandDTO> landList = landRepository.getAllTypeOfApartment().stream().map(
                land -> LandDTO.builder().typeOfApartment(land.getTypeOfApartment()).build()
        ).toList();
        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .data(landList)
                .build();
    }

    @Override
    public ResponseData getAllDirection() {
        List<LandDTO> landList = landRepository.getAllDirection().stream().map(
                land -> LandDTO.builder()
                        .direction(land.getDirection()).build()
        ).toList();
        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .data(landList)
                .build();
    }

    @Override
    public ResponseData filterAllLandsByProjectId(RequestPaginationLandByProjectId request) {
        List<LandDTO> landList = landRepository.getLandPaginationByProjectId(request)
                .stream().map(land -> {
                    LandDTO landDTO = convertLandToLandDTO(land, modelMapper);

                    List<Image> imageList = imageRepository.getImagesByLandId(land.getId());
                    if (!imageList.isEmpty()) {
                        List<ImageDTO> imageDTOList = imageList.stream().map(
                                image -> modelMapper.map(image, ImageDTO.class)
                        ).toList();
                        landDTO.setImages(imageDTOList);
                    }
                    return landDTO;
                }).toList();

        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .data(landList)
                .build();
    }

    private static LandDTO convertLandToLandDTO(Land land, ModelMapper modelMapper){

        LandDTO landDTO = modelMapper.map(land, LandDTO.class);
        if(land.getArea() != null) {
            AreaDTO areaDTO = AreaDTO.builder()
                    .id(land.getArea().getId())
                    .name(land.getArea().getName())
                    .projectId(land.getArea().getProject().getId())
                    .projectName(land.getArea().getProject().getName())
                    .build();
                landDTO.setAreaDTO(areaDTO);
            }
            return landDTO;
    }

}
