package ru.trilla.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("trilla.minio")
public class MinioConfigurationProperties {
    private String endpoint;
    private String username;
    private String password;
}
