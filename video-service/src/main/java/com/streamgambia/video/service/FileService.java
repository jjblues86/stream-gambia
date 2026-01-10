package com.streamgambia.video.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Service
public class FileService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    // 1. FIXED: Constructor Injection initializes minioClient
    public FileService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    // Method for API Uploads (MultipartFile)
    public String uploadFile(MultipartFile file) {
        try {
            // Create bucket if it doesn't exist (Optional, good practice)
            boolean found = minioClient.bucketExists(io.minio.BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(io.minio.MakeBucketArgs.builder().bucket(bucketName).build());
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            // 2. FIXED: Try-Catch block handles all MinIO exceptions
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return fileName;

        } catch (Exception e) {
            // Convert checked exceptions to RuntimeException so compilation passes
            throw new RuntimeException("Error uploading file to MinIO: " + e.getMessage(), e);
        }
    }

    // Method for Transcoder (Standard Java File)
    public void uploadFile(String key, File file) {
        // 2. FIXED: Try-Catch block handles all MinIO exceptions
        try (FileInputStream inputStream = new FileInputStream(file)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(key)
                            .stream(inputStream, file.length(), -1)
                            .contentType("application/octet-stream")
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error uploading transcoded segment to MinIO", e);
        }
    }
}