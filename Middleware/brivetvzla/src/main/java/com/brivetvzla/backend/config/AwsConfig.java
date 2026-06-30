package com.brivetvzla.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsConfig {

    @Bean
    public S3Client s3Client(@Value("${app.aws.region:us-east-1}") String region) {
        // Usa el default credentials provider chain: variables de entorno,
        // ~/.aws/credentials, o IAM role si corre en EC2/ECS/Fargate.
        // No hardcodear access keys en el codigo bajo ninguna circunstancia.
        return S3Client.builder()
                .region(Region.of(region))
                .build();
    }
}
