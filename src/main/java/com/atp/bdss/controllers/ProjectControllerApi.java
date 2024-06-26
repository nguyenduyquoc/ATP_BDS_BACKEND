package com.atp.bdss.controllers;

import com.atp.bdss.dtos.requests.create.RequestCreateProject;
import com.atp.bdss.dtos.requests.pagination.RequestPaginationProject;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import com.atp.bdss.services.IProjectService;
import com.atp.bdss.utils.Constants;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;


@RestController
@RequestMapping(value = Constants.REQUEST_MAPPING_PREFIX + Constants.VERSION_API_V1 + "/projects")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectControllerApi {

    final IProjectService projectService;


    // phân trang dự án
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDataWithPagination allProjects(
            @RequestParam(name = "pageIndex", defaultValue = "0") Short pageIndex,
            @RequestParam(name = "pageSize", defaultValue = "10") Short pageSize,
            @RequestParam(name = "nameProject", required = false) String nameProject,
            @RequestParam(name = "provinceId", required = false) Short provinceId,
            @RequestParam(name = "districtId", required = false) Short districtId,
            @RequestParam(name = "status", required = false) Short status,
            @RequestParam(name = "investor", required = false) String investor,
            @RequestParam(name = "projectTypeId", required = false) Short projectTypeId,
            @RequestParam(name = "priceFrom", required = false) BigDecimal priceFrom,
            @RequestParam(name = "priceTo", required = false) BigDecimal priceTo
    ) {
        RequestPaginationProject requestParam = new RequestPaginationProject();
        requestParam.setPageIndex(pageIndex);
        requestParam.setPageSize(pageSize);
        requestParam.setNameProject(nameProject);
        if (provinceId != null)
            requestParam.setProvinceId(provinceId);
        requestParam.setDistrictId(districtId);
        if (status != null)
            requestParam.setStatus(status);
        requestParam.setInvestor(investor);
        if (projectTypeId != null)
            requestParam.setProjectTypeId(projectTypeId);
        requestParam.setPriceFrom(priceFrom);
        requestParam.setPriceTo(priceTo);
        return projectService.allProjects(requestParam);
    }

    // lay tat ca du an khong phan trang
    @GetMapping(value = "/all-projects", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData allProjectsNoPagination() {

        return projectService.allProjectsNoPagination();
    }


    // tìm dự án theo id
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData getProjectById(@PathVariable String id){

        return projectService.findProjectById(id);
    }

    // tao moi du an
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData createProject(@ModelAttribute @Valid RequestCreateProject request) throws IOException {

        return projectService.create(request);

    }

    // chỉnh sửa dự án (thay đổi trạng thái dự án có thể dùng api này
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData updateProject(@ModelAttribute @Valid RequestCreateProject request) throws IOException {

        return  projectService.updateProject(request);
    }


    // delete project
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData deleteProject(@PathVariable String id)
    {
        return projectService.delete(id);
    }



}
