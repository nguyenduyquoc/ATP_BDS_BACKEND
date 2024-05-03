package com.atp.bdss.services.customService;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public String upload(MultipartFile file) throws IOException {

        Map<String, Object> options = new HashMap<>();
        options.put("resource_type", "image"); // Or use an upload preset
        options.put("folder", "ATP_BDS");

        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
        return uploadResult.get("secure_url").toString();

    }

    public void delete(String publicId) throws IOException {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new IOException("Failed to delete image from Cloudinary: " + e.getMessage());
        }
    }

    public Map<?, ?> uploadImage(MultipartFile multipartFile) throws IOException {
        File file = convert(multipartFile);

        Map<String, Object> options = new HashMap<>();
        options.put("resource_type", "image"); // Or use an upload preset
        options.put("folder", "ATP_BDS");


        Map<?, ?> uploadResult = cloudinary.uploader().upload(file, options);
        if (!Files.deleteIfExists(file.toPath())) {
            throw new IOException("Fail to delete temporary file :" + file.getAbsolutePath());
        }
        return uploadResult;

    }

    public Map<?, ?> deleteImage(String id) throws IOException {
        Map<String, Object> options = new HashMap<>();
        options.put("resource_type", "image"); // Or use an upload preset
        options.put("folder", "ATP_BDS");
        return cloudinary.uploader().destroy(id, options);
    }

    public List<Map<?, ?>> uploadMultiple(MultipartFile[] files) throws IOException {
        List<Map<?, ?>> uploadResults = new ArrayList<>();
        for (MultipartFile file : files) {
            uploadResults.add(uploadImage(file));
        }
        return uploadResults;
    }



    private File convert(MultipartFile multipartFile) throws IOException{
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }





}
