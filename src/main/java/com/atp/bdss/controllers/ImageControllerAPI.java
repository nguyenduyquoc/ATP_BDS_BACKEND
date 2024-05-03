package com.atp.bdss.controllers;

import com.atp.bdss.dtos.requests.RequestCreateImage;
import com.atp.bdss.dtos.requests.RequestCreateLand;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.services.IImageService;
import com.atp.bdss.services.IProjectService;
import com.atp.bdss.utils.Constants;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = Constants.REQUEST_MAPPING_PREFIX + Constants.VERSION_API_V1 + "/images")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ImageControllerAPI {

    final IImageService imageService;

    @PostMapping(value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData createImage(@ModelAttribute @Valid RequestCreateImage request) throws IOException {

        return imageService.save(request);
    }
}
