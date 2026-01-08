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
     * @param objectKey The path inside the bucket (e.g., "videos/123/index.m3u8")
     * @param file The local file to upload
     */
    public void uploadFile(String objectKey, File file) {
        try {
            // Determine Content Type (MIME)
            String contentType = "application/octet-stream";
            if (file.getName().endsWith(".m3u8")) {
                contentType = "application/vnd.apple.mpegurl";
            } else if (file.getName().endsWith(".ts")) {
                contentType = "video/MP2T";
            }

            //Upload the file
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectKey)
                            .filename(file.getAbsolutePath())
                            .contentType(contentType)
                            .build()
            );
            System.out.println("Uploaded '" + file.getName() + "' to '" + objectKey + "'");
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file '" + file.getName() + "' to MinIO", e);
        }
    }
}
