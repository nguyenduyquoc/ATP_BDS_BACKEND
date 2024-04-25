package com.atp.bdss.services.impl;

import com.atp.bdss.dtos.AreaDTO;
import com.atp.bdss.dtos.DistrictDTO;
import com.atp.bdss.dtos.ProjectDTO;
import com.atp.bdss.dtos.ProjectTypeDTO;
import com.atp.bdss.dtos.requests.RequestCreateProject;
import com.atp.bdss.dtos.requests.RequestPaginationProject;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import com.atp.bdss.entities.District;
import com.atp.bdss.entities.Land;
import com.atp.bdss.entities.Project;
import com.atp.bdss.entities.ProjectType;
import com.atp.bdss.exceptions.CustomException;
import com.atp.bdss.repositories.DistrictRepositoryJPA;
import com.atp.bdss.repositories.LandRepositoryJPA;
import com.atp.bdss.repositories.ProjectRepositoryJPA;
import com.atp.bdss.repositories.ProjectTypeRepositoryJPA;
import com.atp.bdss.services.IProjectService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.atp.bdss.utils.CheckerStatus.findStatusProject;
import static com.atp.bdss.utils.UploadImage.uploadImage;


@Component
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectService implements IProjectService {

    final ProjectRepositoryJPA projectRepository;
    final ProjectTypeRepositoryJPA projectTypeRepository;
    final DistrictRepositoryJPA districtRepository;
    final ModelMapper modelMapper;
    final CloudinaryService cloudinary;
    final LandRepositoryJPA landRepository;

    @Override
    public ResponseDataWithPagination allProjects(RequestPaginationProject request) {

        Pageable pageable = PageRequest.of(request.getPageIndex() != null ? request.getPageIndex().intValue() : 0,
                Math.max(request.getPageSize() != null ? request.getPageSize().intValue() : 8, 1));

        if (request.getNameProject() != null)
            request.setNameProject(request.getNameProject().replace("%", "\\%").replace("_", "\\_").trim());

        Page<Project> data = projectRepository.getProjectPagination(request, pageable);

        if(data.isEmpty()){
            throw new CustomException(ErrorsApp.RECORD_NOT_FOUND);
        }

        Page<ProjectDTO> projectDTOPage = data.map(project -> {
            ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);

            if(project.getProjectType() != null) {
                ProjectTypeDTO projectType = modelMapper.map(project.getProjectType(), ProjectTypeDTO.class);
                projectDTO.setProjectType(projectType);
            }

            if(project.getDistrict() != null) {
                DistrictDTO districtDTO =  modelMapper.map(project.getDistrict(), DistrictDTO.class);
                districtDTO.setProvinceName(project.getDistrict().getProvince().getName());
                projectDTO.setDistrict(districtDTO);
            }

            return projectDTO;
        });

        return ResponseDataWithPagination.builder()
                .currentPage(data.getNumber())
                .currentSize(data.getSize())
                .totalRecords((int) data.getTotalElements())
                .totalPages(data.getTotalPages())
                .totalRecordFiltered(data.getNumberOfElements())
                .data(projectDTOPage.getContent())
                .build();
    }

    @Override
    @Transactional
    public ResponseData create(RequestCreateProject request) throws IOException {

        if(projectRepository.existsByNameIgnoreCase(request.getName()))
            throw new CustomException(ErrorsApp.DUPLICATE_PROJECT_NAME);

        ProjectType projectType = projectTypeRepository.findById(request.getProjectTypeId())
                .orElseThrow(() -> new CustomException(ErrorsApp.PROJECT_TYPE_NOT_FOUND));

        District district = districtRepository.findById(request.getDistrictId())
                .orElseThrow(() -> new CustomException(ErrorsApp.DISTRICT_NOT_FOUND));

        if (!findStatusProject(request.getStatus()))
            throw new CustomException(ErrorsApp.STATUS_NOT_FOUND);

        Project project = modelMapper.map(request, Project.class);
        String thumbnail = uploadImage(request.getThumbnail(), cloudinary);
        project.setThumbnail(thumbnail);
        String qrImage = uploadImage(request.getQrImg(), cloudinary);
        project.setQrImg(qrImage);
        project.setCreatedAt(LocalDateTime.now());
        project.setIsDeleted(Constants.ENTITY_STATUS.ACTIVE);
        project.setProjectType(projectType);
        project.setDistrict(district);

        projectRepository.save(project);

        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .build();

    }

    @Override
    public ResponseData findProjectById(String id) {

        Optional<Project> project = projectRepository.findById(id);
        if (project.isEmpty())
            throw new CustomException(ErrorsApp.PROJECT_NOT_FOUND);

        ProjectDTO projectDTO = modelMapper.map(project.get(), ProjectDTO.class);

        List<AreaDTO> areas = new ArrayList<>();
        if (!project.get().getAreas().isEmpty()) {
            areas = project.get().getAreas().stream().map(
                            area -> {
                                AreaDTO areaDTO = modelMapper.map(area, AreaDTO.class);
                                areaDTO.setProjectName(area.getProject().getName());
                                return areaDTO;
                            }
                    )
                    .collect(Collectors.toList());
        }

        projectDTO.setAreas(areas);

        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .data(projectDTO)
                .build();
    }

    @Override
    public ResponseData updateProject(RequestCreateProject request) throws IOException {

        Project project = projectRepository.findById(request.getId())
                .orElseThrow(() -> new CustomException(ErrorsApp.PROJECT_NOT_FOUND));

        if (!project.getName().equalsIgnoreCase(request.getName())) {
            if (projectRepository.existsByNameIgnoreCase(request.getName())) {
                throw new CustomException(ErrorsApp.DUPLICATE_PROJECT_NAME);
            }
            project.setName(request.getName());
        }

        // set projectType
        ProjectType projectType = projectTypeRepository.findById(request.getProjectTypeId())
                .orElseThrow(() -> new CustomException(ErrorsApp.PROJECT_TYPE_NOT_FOUND));
        project.setProjectType(projectType);

        // set District
        District district = districtRepository.findById(request.getDistrictId())
                .orElseThrow(() -> new CustomException(ErrorsApp.DISTRICT_NOT_FOUND));
        project.setDistrict(district);



        // setStatus
        if (!findStatusProject(request.getStatus()))
            throw new CustomException(ErrorsApp.STATUS_NOT_FOUND);
        project.setStatus(request.getStatus());

        // setThumbnail
        if(request.getThumbnail() != null && !request.getThumbnail().isEmpty()){
            String thumbnail = uploadImage(request.getThumbnail(), cloudinary);
            project.setThumbnail(thumbnail);
        }

        // set Qr image
        if(request.getQrImg() != null && !request.getQrImg().isEmpty()){
            String qrImage = uploadImage(request.getQrImg(), cloudinary);
            project.setQrImg(qrImage);
        }
        project.setDescription(request.getDescription());
        project.setAddress(request.getAddress());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setBankName(request.getBankName());
        project.setHostBank(request.getHostBank());
        project.setInvestor(request.getInvestor());
        project.setInvestorPhone(request.getInvestorPhone());
        project.setUpdatedAt(LocalDateTime.now());

        projectRepository.save(project);

        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .build();
    }

    @Override
    public ResponseData allProjectsNoPagination() {
        List<ProjectDTO> projectList = projectRepository.findAll()
                .stream().map(project -> {
                    ProjectDTO projectDTO = new ProjectDTO();
                    projectDTO.setId(project.getId());
                    projectDTO.setName(project.getName());
                    return projectDTO;
                }).toList();
        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .data(projectList)
                .build();
    }

    @Override
    public ResponseData delete(String id) {
        Project project = projectRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorsApp.PROJECT_NOT_FOUND)
        );

        // kiem tra da co lo dat nao ton tai hay chua, neu chua co thi co the xoa
        List<Land> landList = landRepository.getLandFindByProjectId(id);

        if (!landList.isEmpty()) {
            throw new CustomException(ErrorsApp.CAN_NOT_DELETE_PROJECT);
        }
        project.setIsDeleted(Constants.STATUS_ACCOUNT.INACTIVE);
        projectRepository.save(project);
        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .build();
    }


}
