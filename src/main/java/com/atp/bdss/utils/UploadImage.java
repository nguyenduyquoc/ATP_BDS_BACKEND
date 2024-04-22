package com.atp.bdss.utils;

import com.atp.bdss.services.customService.CloudinaryService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class UploadImage {

    public static String uploadImage(MultipartFile image, CloudinaryService cloudinaryService) throws IOException {
        String thumbnail = null;
        if(image != null && !image.isEmpty())
            thumbnail = cloudinaryService.upload(image);
        return thumbnail;
    }
}
