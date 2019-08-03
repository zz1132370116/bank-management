package com.zl.dc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 自定义资源映射
 * 设置虚拟路径，访问绝对路径下资源
 */

@ComponentScan
@Configuration
public class UploadUrlConfigurer extends WebMvcConfigurerAdapter {
    @Autowired
    PreReadUploadConfig uploadConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("file:///"+uploadConfig.getUploadPath());
    }
}
