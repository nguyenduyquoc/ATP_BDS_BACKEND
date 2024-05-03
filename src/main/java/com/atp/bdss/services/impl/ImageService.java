package com.atp.bdss.services.impl;

import com.atp.bdss.dtos.requests.create.RequestCreateImage;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.entities.Image;
import com.atp.bdss.entities.Land;
import com.atp.bdss.exceptions.CustomException;
import com.atp.bdss.repositories.ImageRepositoryJPA;
import com.atp.bdss.repositories.LandRepositoryJPA;
import com.atp.bdss.services.IImageService;
import com.atp.bdss.services.customService.CloudinaryService;
import com.atp.bdss.utils.ErrorsApp;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageService implements IImageService {

    final CloudinaryService cloudinary;
    final ImageRepositoryJPA imageRepository;
    final LandRepositoryJPA landRepository;

    @Override
    public List<Image> list() {
        return null;
    }

    @Override
    public ResponseData getImage(int id) {
        return null;
    }

    @Override
    public ResponseData save(RequestCreateImage request) throws IOException {

        BufferedImage bufferedImage = ImageIO.read(request.getUrl().getInputStream());
        if (bufferedImage == null)
            throw new CustomException(ErrorsApp.LAND_NOT_FOUND);
        Map result = cloudinary.uploadImage(request.getUrl());

        Land land = landRepository.findById(request.getLandId()).orElseThrow(
                () -> new CustomException(ErrorsApp.LAND_NOT_FOUND)
        );

        Image image = Image.builder()
                .url((String) result.get("url"))
                .description((String) result.get("original_filename"))
                .imageId((String) result.get("public_id"))
                .land(land)
                .build();
        imageRepository.save(image);
        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .build();
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public boolean exist(int id) {
        return false;
    }
}
