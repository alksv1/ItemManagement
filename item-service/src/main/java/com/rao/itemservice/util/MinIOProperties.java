package com.rao.itemservice.util;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinIOProperties {

    private String bucketName;
    private String endpoint;
    private String accessKeySecret;
    private String accessKeyId;
}
