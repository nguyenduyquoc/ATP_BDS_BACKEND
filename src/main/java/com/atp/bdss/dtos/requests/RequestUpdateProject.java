package com.atp.bdss.dtos.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestUpdateProject {

    String id;

    String name;

    String description;

    String thumbnail;

    MultipartFile thumbnailNew;

    short status;

    String address;

    LocalDate startDate;

    LocalDate endDate;

    String qrImg;

    MultipartFile qrImgNew;

    String bankNumber;

    String bankName;

    String hostBank;

    String investor;

    String investorPhone;

    short projectTypeId;

    int districtId;
}
