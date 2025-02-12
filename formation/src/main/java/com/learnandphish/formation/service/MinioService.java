package com.learnandphish.formation.service;

import io.minio.*;
import io.minio.errors.MinioException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class MinioService {

    @Value("${s3.bucket}")
    private String bucketName;

    @Autowired
    private MinioClient minioClient;

    private static final Logger log = LoggerFactory.getLogger(MinioService.class);

    @PostConstruct
    private void init() {
        ensureBucketPolicy();
    }

    private void ensureBucketPolicy() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());

            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
            }

            String currentPolicy = minioClient.getBucketPolicy(
                    GetBucketPolicyArgs.builder().bucket(bucketName).build()
            );

            if (currentPolicy == null || currentPolicy.isEmpty()) {
                String policy = """
                        {
                            "Version": "2012-10-17",
                            "Statement": [
                                {
                                    "Effect": "Allow",
                                    "Principal": "*",
                                    "Action": ["s3:GetObject"],
                                    "Resource": ["arn:aws:s3:::%s/*"]
                                }
                            ]
                        }
                        """.formatted(bucketName);

                minioClient.setBucketPolicy(
                        SetBucketPolicyArgs.builder()
                                .bucket(bucketName)
                                .config(policy)
                                .build()
                );
                log.info("Bucket policy set successfully for bucket: {}", bucketName);
            }
        } catch (Exception e) {
            log.error("Error ensuring bucket policy: {}", e.getMessage(), e);
        }
    }

    public String uploadFile(File file) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            String objectName = String.valueOf(UUID.randomUUID());
            String filePath = file.getAbsolutePath();

            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .filename(filePath)
                            .build());
            log.info("File '{}' is successfully uploaded as object '{}' to bucket '{}'.", filePath, objectName, bucketName);
            return "/" + bucketName + "/" + objectName;
        } catch (MinioException e) {
            log.error("Error occurred: {}", e.getMessage());
            log.error("HTTP trace: {}", e.httpTrace());
        }
        return null;
    }


    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if (!isAllowedContentType(file.getContentType())) {
            throw new IllegalArgumentException("File type not allowed");
        }
        try {
            String objectName = String.valueOf(UUID.randomUUID());
            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build());
                log.info("File '{}' is successfully uploaded as object '{}' to bucket '{}'.", file.getOriginalFilename(), objectName, bucketName);
                return endpoint + "/" + bucketName + "/" + objectName;
            } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        } catch (MinioException e) {
            log.error("Error occurred: {}", e.getMessage());
            log.error("HTTP trace: {}", e.httpTrace());
            return null;
        } catch (IOException e) {
            log.error("Failed to read file", e);
            return null;
        }
    }

    private boolean isAllowedContentType(String contentType) {
        return contentType != null && (contentType.startsWith("video/") || contentType.startsWith("image/") || contentType.equals("text/vtt"));
    }

    public void deleteFile(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL is empty");
        }
        String objectName = extractObjectName(url);
        if (objectName.isEmpty()) {
            throw new IllegalArgumentException("Invalid URL");
        }
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
            log.info("File '{}' is successfully deleted from bucket '{}'.", objectName, bucketName);
        } catch (MinioException e) {
            log.error("Error occurred: {}", e.getMessage());
            log.error("HTTP trace: {}", e.httpTrace());
            throw new RuntimeException(e);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Failed to delete file", e);
            throw new RuntimeException(e);
        }
    }

    private String extractObjectName(String url) {
        try {
            String[] parts = url.split("/");
            return parts[parts.length - 1];
        } catch (ArrayIndexOutOfBoundsException e) {
            return "";
        }
    }
}