package com.atp.bdss.services.impl;


import com.atp.bdss.dtos.requests.create.RequestCreateArea;
import com.atp.bdss.dtos.AreaDTO;
import com.atp.bdss.dtos.LandDTO;
import com.atp.bdss.dtos.requests.create.RequestCreateMultiObject;
import com.atp.bdss.dtos.requests.pagination.RequestPaginationArea;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import com.atp.bdss.entities.Area;
import com.atp.bdss.entities.Project;
import com.atp.bdss.exceptions.CustomException;
import com.atp.bdss.repositories.*;
import com.atp.bdss.services.IAreaService;
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

import java.util.*;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AreaService implements IAreaService {

    final ProjectRepositoryJPA projectRepository;
    final AreaRepositoryJPA areaRepository;
    final ModelMapper modelMapper;

    @Override
    public ResponseDataWithPagination allAreasWithPagination(RequestPaginationArea request) {
        Pageable pageable = PageRequest.of(request.getPageIndex() != null ? request.getPageIndex().intValue() : 0,
                Math.max(request.getPageSize() != null ? request.getPageSize().intValue() : 8, 1));

        if (request.getAreaName() != null)
            request.setAreaName(request.getAreaName().replace("%", "\\%").replace("_", "\\_").trim());

        Page<AreaDTO> data = areaRepository.getAreaPagination(request, pageable);
        if(data.isEmpty()){
            throw new CustomException(ErrorsApp.RECORD_NOT_FOUND);
        }
        return ResponseDataWithPagination
                .builder()
                .currentPage(data.getNumber())
                .currentSize(data.getSize())
                .totalRecords((int) data.getTotalElements())
                .totalPages(data.getTotalPages())
                .totalRecordFiltered(data.getContent().size())
                .data(data.getContent())
                .build()
                ;
    }

    @Override
    public ResponseData createAreaForProject(RequestCreateArea request) {

        // check xem project nay có ton tai khong
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new CustomException(ErrorsApp.PROJECT_NOT_FOUND));

        // Kiểm tra xem tên khu vực đã tồn tại trong dự án hay chưa
        if (areaRepository.existsByNameIgnoreCaseAndProjectId(request.getName(), request.getProjectId()))
            throw new CustomException(ErrorsApp.DUPLICATE_AREA_NAME);

        Area area = Area.builder()
                .name(request.getName())
                .project(project)
                .build();
        areaRepository.save(area);

        return ResponseData
                .builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .build();
    }

    @Override
    public ResponseData createAreaMultiProject(RequestCreateMultiObject<RequestCreateArea> request) {
        // check xem id nay có ton tai khong
        Project project =  projectRepository.findById(request.getId())
                .orElseThrow(() -> new CustomException(ErrorsApp.PROJECT_NOT_FOUND));

        Set<String> areaNames = new HashSet<>();

        if (request.getMultiObject().isEmpty())
            throw new CustomException(ErrorsApp.BAD_REQUEST);

        for (RequestCreateArea area : request.getMultiObject()) {
            // Kiểm tra xem tên đối tượng đã tồn tại trong danh sách hay chưa
            if (!areaNames.add(area.getName().toLowerCase())) {
                throw new CustomException(ErrorsApp.DUPLICATE_AREA_NAME);
            }

            // Kiểm tra đối tượng với cơ sở dữ liệu
            if (areaRepository.existsByNameIgnoreCase(area.getName())) {
                throw new CustomException(ErrorsApp.PROJECT_TYPE_NOT_FOUND);
            }
        }

        List<Area> areaList = project.getAreas();

        for(RequestCreateArea areaCreate : request.getMultiObject()) {
            Area area = Area.builder()
                    .name(areaCreate.getName())
                    .project(project)
                    .build();

            areaList.add(area);
        }
        project.setAreas(areaList);

        projectRepository.save(project);

        return ResponseData
                .builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .build();
    }

    @Override
    public ResponseData updateAreaForProject(RequestCreateArea request) {
        Area area = areaRepository.findById(request.getId())
                .orElseThrow(() -> new CustomException(ErrorsApp.AREA_NOT_FOUND));

        // check xem project nay có ton tai khong
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new CustomException(ErrorsApp.PROJECT_NOT_FOUND));
        area.setProject(project);

        // check xem name truyen vao da trung voi name thuoc du an nay hay chua
        if (!area.getName().equalsIgnoreCase(request.getName())) {
            if (areaRepository.existsByNameIgnoreCaseAndProjectId(request.getName(), request.getProjectId())) {
                throw new CustomException(ErrorsApp.DUPLICATE_PROJECT_NAME);
            }
            area.setName(request.getName());
        }

        areaRepository.save(area);

        return ResponseData
                .builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .build();
    }

    @Override
    public ResponseData findAreaById(String id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorsApp.AREA_NOT_FOUND));

        AreaDTO areaDTO = modelMapper.map(area, AreaDTO.class);
        List<LandDTO> landDTOS;
        if (!area.getLands().isEmpty()) {
            landDTOS = area.getLands().stream()
                    .map(land -> modelMapper.map(land, LandDTO.class))
                    .collect(Collectors.toList());

            areaDTO.setLands(landDTOS);
        }
        return ResponseData
                .builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .data(areaDTO)
                .build();
    }

    @Override
    public ResponseData delete(String id) {
        Area area = areaRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorsApp.AREA_NOT_FOUND)
        );

        // kiem tra da co lo dat nao ton tai hay chua, neu chua co thi co the xoa
        if (!area.getLands().isEmpty()) {
            throw new CustomException(ErrorsApp.CAN_NOT_DELETE_PROJECT);
        }

        areaRepository.deleteById(id);
        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .build();
    }

    @Override
    public ResponseData allAreasNoPagination() {
        List<AreaDTO> areas =  areaRepository.findAll().stream().map(
                area -> AreaDTO.builder()
                        .id(area.getId())
                        .name(area.getName())
                        .projectId(area.getProject().getId())
                        .projectName(area.getProject().getName())
                        .build()
        ).toList();

        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .data(areas)
                .build();
    }

}
