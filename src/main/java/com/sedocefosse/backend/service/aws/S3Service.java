package com.sedocefosse.backend.service.aws;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String uploadImage(MultipartFile file, String folder);
    String uploadImage(MultipartFile file, String folder, String fileName);
    String uploadImageFromBase64(String base64Image, String folder);
    void deleteImage(String imageUrl);
}

