package com.rao.userservice.util;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "email.retrieve")
public class EmailProperties {

    private String from;
    private String subject;
    private String content;
}
