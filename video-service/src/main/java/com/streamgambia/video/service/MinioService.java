package com.streamgambia.video.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class MinioService {

    // Inject values from application.yml
    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucket:videos}")
    private String bucketName;

    private MinioClient minioClient;

    // Initialize the client when the application starts
    @PostConstruct
    public void init() {
        try {
            minioClient = MinioClient.builder()
                    .endpoint(minioUrl)
                    .credentials(accessKey, secretKey)
                    .build();

            // Create the bucket if it doesn't exist
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());

            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                System.out.println("Bucket ' " + bucketName + "' created.");
            } else {
                System.out.println("Bucket ' " + bucketName + "' already exists.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error initializing MinIO", e);
        }
    }

    /**
     * Uploads a file from the local disk to MinIO
     * @param key The path inside the bucket (e.g., "videos/123/index.m3u8")
     * @param file The local file to upload
     */
    public void uploadFile(String key, java.io.File file) {
        try (java.io.FileInputStream inputStream = new java.io.FileInputStream(file)) {
            minioClient.putObject(
                    io.minio.PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(key)
                            .stream(inputStream, file.length(), -1)
                            .contentType("application/octet-stream")
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to MinIO", e);
        }
    }
}
