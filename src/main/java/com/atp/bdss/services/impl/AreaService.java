package com.atp.bdss.services.impl;


import com.atp.bdss.dtos.AreaCreate;
import com.atp.bdss.dtos.AreaDTO;
import com.atp.bdss.dtos.LandDTO;
import com.atp.bdss.dtos.requests.RequestCreateMultiObject;
import com.atp.bdss.dtos.requests.RequestPaginationArea;
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
    public ResponseDataWithPagination allAreas(RequestPaginationArea request) {
        Pageable pageable;
        long pageIndex = request.getPageIndex() != null ? request.getPageIndex() : 0;
        long pageSize = request.getPageSize() != null ? request.getPageSize() : 10;
        long adjustedPageSize = Math.max(1, pageSize);

        pageable = PageRequest.of((int) pageIndex, (int) adjustedPageSize);

        if (request.getAreaName() != null)
            request.setAreaName(request.getAreaName().replace("%", "\\%").replace("_", "\\_").trim());

        Page<AreaDTO> data = areaRepository.getAreaPagination(request, pageable);
        if(data.isEmpty()){
            throw new CustomException(ErrorsApp.RECORD_NOT_FOUND);
        }
        return ResponseDataWithPagination
                .builder()
                .currentPage((int) pageIndex)
                .currentSize((int) adjustedPageSize)
                .totalRecords((int) data.getTotalElements())
                .totalPages(data.getTotalPages())
                .totalRecordFiltered(data.getContent().size())
                .data(data.getContent())
                .build()
                ;
    }

    @Override
    public ResponseData createAreaForProject(AreaCreate request) {

        // check xem id nay có ton tai khong
        Optional<Project> project = projectRepository.findById(request.getProjectId());
        if (project.isEmpty())
            throw new CustomException(ErrorsApp.RECORD_NOT_FOUND);

        // check xem name truyen vao da trung voi name thuoc du an nay hay chua
        List<Area> areasInProject = project.get().getAreas();

        boolean nameExistsInProject = areasInProject.stream()
                .anyMatch(area -> area.getName().equalsIgnoreCase(request.getName()));

        if (nameExistsInProject) {
            throw new CustomException(ErrorsApp.DUPLICATE_AREA_NAME);
        }

        Area area = Area.builder()
                .name(request.getName())
                .project(project.get())
                .build();
        areaRepository.save(area);

        return ResponseData
                .builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .build();
    }

    @Override
    public ResponseData createAreaMultiProject(RequestCreateMultiObject<AreaCreate> request) {
        // check xem id nay có ton tai khong
        Optional<Project> optionalProject =  projectRepository.findById(request.getId());
        if(optionalProject.isEmpty())
            throw new CustomException(ErrorsApp.RECORD_NOT_FOUND);

        Set<String> areaNames = new HashSet<>();

        if (request.getMultiObject().isEmpty())
            throw new CustomException(ErrorsApp.BAD_REQUEST);

        for (AreaCreate area : request.getMultiObject()) {
            // Kiểm tra xem tên đối tượng đã tồn tại trong danh sách hay chưa
            if (!areaNames.add(area.getName().toLowerCase())) {
                throw new CustomException(ErrorsApp.DUPLICATE_AREA_NAME);
            }

            // Kiểm tra đối tượng với cơ sở dữ liệu
            if (areaRepository.existsByNameIgnoreCase(area.getName())) {
                throw new CustomException(ErrorsApp.PROJECT_TYPE_NOT_FOUND);
            }
        }

        List<Area> areaList = optionalProject.get().getAreas();

        for(AreaCreate areaCreate : request.getMultiObject()) {
            Area area = Area.builder()
                    .name(areaCreate.getName())
                    .project(optionalProject.get())
                    .build();

            areaList.add(area);
        }
        optionalProject.get().setAreas(areaList);

        projectRepository.save(optionalProject.get());

        return ResponseData
                .builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .build();
    }

    @Override
    public ResponseData updateAreaForProject(AreaCreate request) {
        Optional<Area> optionArea = areaRepository.findById(request.getId());
        if (optionArea.isEmpty())
            throw new CustomException(ErrorsApp.RECORD_NOT_FOUND);

        if(!optionArea.get().getLands().isEmpty())
            throw new CustomException(ErrorsApp.LAND_EXISTED);

        // check xem id nay có ton tai khong
        Optional<Project> project = projectRepository.findById(request.getProjectId());
        if (project.isEmpty())
            throw new CustomException(ErrorsApp.RECORD_NOT_FOUND);

        // check xem name truyen vao da trung voi name thuoc du an nay hay chua
        List<Area> areasInProject = project.get().getAreas();

        boolean nameExistsInProject = areasInProject.stream()
                .anyMatch(area -> area.getName().equalsIgnoreCase(request.getName()));

        if (nameExistsInProject) {
            throw new CustomException(ErrorsApp.DUPLICATE_AREA_NAME);
        }

        Area area = Area.builder()
                .id(request.getId())
                .name(request.getName())
                .project(project.get())
                .build();
        areaRepository.save(area);

        return ResponseData
                .builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .build();
    }

    @Override
    public ResponseData findAreaById(String id) {
        Optional<Area> area = areaRepository.findById(id);
        if (area.isEmpty())
            throw new CustomException(ErrorsApp.RECORD_NOT_FOUND);

        AreaDTO areaDTO = modelMapper.map(area.get(), AreaDTO.class);
        List<LandDTO> landDTOS = new ArrayList<>();
        if (!area.get().getLands().isEmpty()) {
            landDTOS = area.get().getLands().stream()
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

}
