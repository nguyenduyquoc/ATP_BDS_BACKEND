package com.atp.bdss.services;

import com.atp.bdss.dtos.requests.create.RequestCreateImage;
import com.atp.bdss.dtos.requests.create.RequestCreateMultiImageForALand;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.entities.Image;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface IImageService {
    List<Image> list();

    ResponseData getImage(int id);

    ResponseData save(RequestCreateImage request) throws IOException;

    void delete(int id);

    boolean exist(int id);

    ResponseData importMultiImageForALand(RequestCreateMultiImageForALand request) throws IOException;
}

