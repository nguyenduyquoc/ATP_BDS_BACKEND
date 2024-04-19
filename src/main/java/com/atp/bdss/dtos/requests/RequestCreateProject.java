package com.atp.bdss.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RequestCreateProject {

    String id;

    String name;

    String description;

    String thumbnail;

    short status;

    String address;

    LocalDate startDate;

    LocalDate endDate;

    String qrImg;

    String bankNumber;

    String bankName;

    String hostBank;

    String investor;

    String investorPhone;

    short projectTypeId;

    int districtId;
}
