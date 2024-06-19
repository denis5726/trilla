package ru.trilla.config;

import io.minio.MinioClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MinioConfigurationProperties.class)
public class MinioConfig {

    @Bean
    @ConfigurationProperties("trilla.minio")
    MinioClient minioClient(MinioConfigurationProperties properties) {
        return MinioClient.builder()
                .endpoint(properties.getEndpoint())
                .credentials(properties.getUsername(), properties.getPassword())
                .build();
    }
}
