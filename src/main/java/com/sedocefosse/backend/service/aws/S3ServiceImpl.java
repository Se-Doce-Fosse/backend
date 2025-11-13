package com.sedocefosse.backend.service.aws;

import com.sedocefosse.backend.configs.exceptions.FileUploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class S3ServiceImpl implements S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    public S3ServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/webp"
    );

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    @Override
    public String uploadImage(MultipartFile file, String folder) {
        String fileName = generateFileName(file.getOriginalFilename());
        return uploadImage(file, folder, fileName);
    }

    @Override
    public String uploadImage(MultipartFile file, String folder, String fileName) {
        validateFile(file);

        try {
            String key = folder != null && !folder.isEmpty() 
                    ? folder + "/" + fileName 
                    : fileName;

            String contentType = file.getContentType();
            if (contentType == null) {
                contentType = "image/jpeg"; // Default
            }

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(
                    file.getInputStream(), file.getSize()));

            String imageUrl = String.format("https://%s.s3.%s.amazonaws.com/%s",
                    bucketName,
                    region,
                    key);

            log.info("Imagem enviada com sucesso para S3: {}", imageUrl);
            return imageUrl;

        } catch (IOException e) {
            log.error("Erro ao ler o arquivo: {}", e.getMessage());
            throw new FileUploadException("Erro ao processar o arquivo: " + e.getMessage(), e);
        } catch (S3Exception e) {
            log.error("Erro ao fazer upload para S3: {}", e.getMessage());
            throw new FileUploadException("Erro ao fazer upload para S3: " + e.getMessage(), e);
        }
    }

    @Override
    public String uploadImageFromBase64(String base64Image, String folder) {
        try {
            String base64Data = base64Image;
            String contentType = "image/jpeg"; 
            String extension = ".jpg";
            
            if (base64Image.contains(",")) {
                String[] parts = base64Image.split(",");
                base64Data = parts[1];
                
                String prefix = parts[0];
                if (prefix.contains("image/jpeg") || prefix.contains("image/jpg")) {
                    contentType = "image/jpeg";
                    extension = ".jpg";
                } else if (prefix.contains("image/png")) {
                    contentType = "image/png";
                    extension = ".png";
                } else if (prefix.contains("image/webp")) {
                    contentType = "image/webp";
                    extension = ".webp";
                }
            }
            
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);
            
            if (imageBytes.length > MAX_FILE_SIZE) {
                throw new FileUploadException("Arquivo muito grande. Tamanho máximo permitido: 5MB");
            }
            
            String fileName = generateFileName("image" + extension);
            String key = folder != null && !folder.isEmpty() 
                    ? folder + "/" + fileName 
                    : fileName;
            
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();
            
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(
                    new ByteArrayInputStream(imageBytes), imageBytes.length));
            
            String imageUrl = String.format("https://%s.s3.%s.amazonaws.com/%s",
                    bucketName,
                    region,
                    key);
            
            log.info("Imagem enviada com sucesso para S3 a partir de Base64: {}", imageUrl);
            return imageUrl;
            
        } catch (IllegalArgumentException e) {
            log.error("Erro ao decodificar Base64: {}", e.getMessage());
            throw new FileUploadException("Formato Base64 inválido: " + e.getMessage(), e);
        } catch (S3Exception e) {
            log.error("Erro ao fazer upload para S3: {}", e.getMessage());
            throw new FileUploadException("Erro ao fazer upload para S3: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteImage(String imageUrl) {
        try {
            String key = extractKeyFromUrl(imageUrl);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("Imagem deletada com sucesso do S3: {}", key);

        } catch (S3Exception e) {
            log.error("Erro ao deletar imagem do S3: {}", e.getMessage());
            throw new FileUploadException("Erro ao deletar imagem do S3: " + e.getMessage(), e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileUploadException("Arquivo não pode ser vazio");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new FileUploadException(
                    "Tipo de arquivo não permitido. Use apenas: JPG, PNG ou WEBP");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileUploadException(
                    "Arquivo muito grande. Tamanho máximo permitido: 5MB");
        }
    }

    private String generateFileName(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        } else {
            extension = ".jpg"; 
        }

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);

        return timestamp + "-" + uuid + extension;
    }

    private String extractKeyFromUrl(String imageUrl) {
        try {
            String[] parts = imageUrl.split(".amazonaws.com/");
            if (parts.length > 1) {
                return parts[1];
            }
            throw new FileUploadException("URL de imagem inválida");
        } catch (Exception e) {
            throw new FileUploadException("Erro ao extrair chave da URL: " + e.getMessage());
        }
    }
}

