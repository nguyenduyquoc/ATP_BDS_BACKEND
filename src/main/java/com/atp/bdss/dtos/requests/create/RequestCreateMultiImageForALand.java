package com.atp.bdss.dtos.requests.create;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestCreateMultiImageForALand {

    String landId;

    MultipartFile[] files;
}
