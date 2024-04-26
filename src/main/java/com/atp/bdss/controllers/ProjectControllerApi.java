package com.atp.bdss.controllers;

import com.atp.bdss.dtos.requests.*;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import com.atp.bdss.services.IProjectService;
import com.atp.bdss.utils.Constants;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

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
        requestParam.setProvinceId(provinceId);
        requestParam.setDistrictId(districtId);
        if (status != null)
            requestParam.setStatus(status);
        requestParam.setInvestor(investor);
        requestParam.setProjectTypeId(projectTypeId);
        requestParam.setPriceFrom(priceFrom);
        requestParam.setPriceTo(priceTo);
        return projectService.allProjects(requestParam);
    }

    // lay tat ca du an khong phan trang
    @GetMapping(value = "/allProjects", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData allProjectsNoPagination() {
        return projectService.allProjectsNoPagination();
    }


    // tìm dự án theo id
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData getProjectById(@PathVariable String id){

        return projectService.findProjectById(id);
    }

    // tao moi du an
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData createProject(@RequestParam("name") String name,
                                      @RequestParam("description") String description,
                                      @RequestParam(value = "thumbnail", required = false) MultipartFile image,
                                      @RequestParam("status") String status,
                                      @RequestParam("address") String address,
                                      @RequestParam("startDate") String startDate,
                                      @RequestParam("endDate") String endDate,
                                      @RequestParam(value = "qrImg", required = false) MultipartFile qrImg,
                                      @RequestParam("bankNumber") String bankNumber,
                                      @RequestParam("bankName") String bankName,
                                      @RequestParam("hostBank") String hostBank,
                                      @RequestParam("investor") String investor,
                                      @RequestParam("investorPhone") String investorPhone,
                                      @RequestParam("projectTypeId") String projectTypeId,
                                      @RequestParam("districtId") String districtId) throws IOException {
        RequestCreateProject request = RequestCreateProject.builder()
                .name(name)
                .description(description)
                .thumbnail(image)
                .status(Short.parseShort(status))
                .address(address)
                .startDate(LocalDate.parse(startDate))
                .endDate(LocalDate.parse(endDate))
                .qrImg(qrImg)
                .bankNumber(bankNumber)
                .bankName(bankName)
                .hostBank(hostBank)
                .investor(investor)
                .investorPhone(investorPhone)
                .projectTypeId(Short.parseShort(projectTypeId))
                .districtId(Integer.parseInt(districtId))
                .build();
        return projectService.create(request);

    }

    // chỉnh sửa dự án (thay đổi trạng thái dự án có thể dùng api này)
    @PutMapping(value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData updateProject(@RequestParam("id") String id,
                                      @RequestParam("name") String name,
                                      @RequestParam("description") String description,
                                      @RequestParam(value = "thumbnail", required = false) MultipartFile image,
                                      @RequestParam("status") String status,
                                      @RequestParam("address") String address,
                                      @RequestParam("startDate") String startDate,
                                      @RequestParam("endDate") String endDate,
                                      @RequestParam(value = "qrImg", required = false) MultipartFile qrImg,
                                      @RequestParam("bankNumber") String bankNumber,
                                      @RequestParam("bankName") String bankName,
                                      @RequestParam("hostBank") String hostBank,
                                      @RequestParam("investor") String investor,
                                      @RequestParam("investorPhone") String investorPhone,
                                      @RequestParam("projectTypeId") String projectTypeId,
                                      @RequestParam("districtId") String districtId) throws IOException {

        RequestCreateProject request = RequestCreateProject.builder()
                .id(id)
                .name(name)
                .description(description)
                .thumbnail(image)
                .status(Short.parseShort(status))
                .address(address)
                .startDate(LocalDate.parse(startDate))
                .endDate(LocalDate.parse(endDate))
                .qrImg(qrImg)
                .bankNumber(bankNumber)
                .bankName(bankName)
                .hostBank(hostBank)
                .investor(investor)
                .investorPhone(investorPhone)
                .projectTypeId(Short.parseShort(projectTypeId))
                .districtId(Integer.parseInt(districtId))
                .build();
        return  projectService.updateProject(request);
    }


    // delete project
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData deleteProject(@PathVariable String id)
    {
        return projectService.delete(id);
    }



}
