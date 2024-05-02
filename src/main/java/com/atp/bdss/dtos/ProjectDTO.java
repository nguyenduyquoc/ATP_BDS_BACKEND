package com.atp.bdss.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDTO {

    String id;

    String name;

    String description;

    String thumbnail;

    String address;

    short status;

    LocalDate startDate;

    LocalDate endDate;

    String qrImg;

    String bankNumber;

    String bankName;

    String hostBank;

    String investor;

    String investorPhone;

    ProjectTypeDTO projectType;

    List<AreaDTO> areas;

    DistrictDTO district;

    public ProjectDTO(String id, String name, String description, String thumbnail, String address, short status, String qrImg,
                      String bankNumber, String bankName, String hostBank, String investor, String investorPhone, LocalDate startDate,
                      LocalDate endDate, ProjectTypeDTO projectType, DistrictDTO district) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
        this.address = address;
        this.status = status;
        this.qrImg = qrImg;
        this.bankNumber = bankNumber;
        this.bankName = bankName;
        this.hostBank = hostBank;
        this.investor = investor;
        this.investorPhone = investorPhone;
        this.startDate = startDate;
        this.endDate = endDate;
        this.projectType = projectType;
        this.district = district;
    }

    public ProjectDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
