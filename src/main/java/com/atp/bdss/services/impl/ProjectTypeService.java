package com.atp.bdss.services.impl;

import com.atp.bdss.dtos.ProjectTypeDTO;
import com.atp.bdss.dtos.requests.create.RequestCreateProjectType;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.entities.ProjectType;
import com.atp.bdss.exceptions.CustomException;
import com.atp.bdss.repositories.ProjectTypeRepositoryJPA;
import com.atp.bdss.services.IProjectTypeService;
import com.atp.bdss.utils.ErrorsApp;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectTypeService implements IProjectTypeService {

    final ProjectTypeRepositoryJPA projectTypeRepository;
    final ModelMapper modelMapper;

    @Override
    public ResponseData listProjectType() {
        List<ProjectTypeDTO> list = projectTypeRepository.findAll().stream().map(
                projectType -> modelMapper.map(projectType, ProjectTypeDTO.class)
        ).toList();
        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .data(list)
                .build();
    }

    @Override
    public ResponseData create(RequestCreateProjectType request) {
        boolean existedProjectType = projectTypeRepository.existsByNameIgnoreCase(request.getName());
        if (existedProjectType)
            throw new CustomException(ErrorsApp.PROJECT_TYPE_EXISTED);
        ProjectType projectType = ProjectType.builder()
                .name(request.getName()).build();
        projectTypeRepository.save(projectType);

        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .build();
    }

    @Override
    public ResponseData update(ProjectTypeDTO request) {
        ProjectType projectType = projectTypeRepository.findById(request.getId()).orElseThrow(
                () -> new CustomException(ErrorsApp.PROJECT_TYPE_NOT_FOUND)
        );
        boolean existed = projectTypeRepository.existsByNameIgnoreCase(request.getName());
        if (existed) throw new CustomException(ErrorsApp.PROJECT_TYPE_EXISTED);
        projectType.setName(request.getName());
        projectTypeRepository.save(projectType);

        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .build();
    }

    @Override
    public ResponseData delete(Short id) {
        ProjectType projectType = projectTypeRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorsApp.PROJECT_TYPE_NOT_FOUND)
        );
        if (!projectType.getProjects().isEmpty())
            throw new CustomException(ErrorsApp.CAN_NOT_DELETE_PROJECT_TYPE);
        projectTypeRepository.deleteById(id);

        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .build();
    }

    @Override
    public ResponseData findProjectTypeById(Short id) {
        ProjectType projectType = projectTypeRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorsApp.PROJECT_TYPE_NOT_FOUND)
        );
        ProjectTypeDTO projectTypeDTO = modelMapper.map(projectType, ProjectTypeDTO.class);

        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .data(projectTypeDTO)
                .build();
    }
}
