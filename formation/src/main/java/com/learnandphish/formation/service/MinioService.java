package com.learnandphish.formation.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
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


    public String uploadFile(File file) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            } else {
                log.warn("Bucket already exists");
            }

            String objectName = UUID.randomUUID() + "-" + file.getName();
            String filePath = file.getAbsolutePath();

            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .filename(filePath)
                            .build());
            log.info("File '{}' is successfully uploaded as object '{}' to bucket '{}'.", filePath, objectName, bucketName);
            return System.getenv("S3_ENDPOINT") + "/" + bucketName + "/" + objectName;
        } catch (MinioException e) {
            log.error("Error occurred: {}", e.getMessage());
            log.error("HTTP trace: {}", e.httpTrace());
        }
        return null;
    }
}