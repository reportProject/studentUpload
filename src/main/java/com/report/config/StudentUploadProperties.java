package com.report.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "net.skhu.upload")
public class StudentUploadProperties {
    String localPath;
    String urlPath;
}
