package com.learnandphish.formation.service;

import io.minio.*;
import io.minio.errors.MinioException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
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
}