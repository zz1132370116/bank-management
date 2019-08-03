package com.zl.dc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件上传路径配置
 */
@Component
@ConfigurationProperties(prefix="preread")
public class PreReadUploadConfig {

    //上传路径
    private String uploadPath;

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }
}
