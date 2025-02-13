package com.learnandphish.formation.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Value("${s3.endpoint}")
    private String endpoint;

    @Value("${s3.access.key}")
    private String accessKey;

    @Value("${s3.secret.key}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        if (endpoint.equals("http://api-gateway:8080") || endpoint.equals("http://localhost:8080")) {
            return MinioClient.builder()
                    .endpoint("http://s3:9000")
                    .credentials(accessKey, secretKey)
                    .build();
        } else {
            return MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();
        }
    }
}