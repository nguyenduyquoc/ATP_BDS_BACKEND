package com.atp.bdss.dtos.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestCreateLand {

    String id;

    String name;

    String description;

    MultipartFile thumbnail;

    String address;

    short status;

    BigDecimal price;

    BigDecimal deposit;

    long acreage;

    String areaId;
}
