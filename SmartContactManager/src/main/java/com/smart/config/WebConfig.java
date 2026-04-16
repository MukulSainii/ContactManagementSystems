package com.smart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Boot doesnot know how to access folder outside the project.
 * So, it’s a resource mapping between a URL path(project path image store) and a file system location.
 * It is called Static Resource Mapping (Resource Handler Mapping).
 * serve static files (like uploaded images) from your file system through a URL in your Spring Boot application.
 * /Image/**  →  file:<uploadDir>/ 
 * when request come like http://localhost:8080/Image/test.jpg convert to <uploadDir>/test.jpg(C:/uploads/photo.jpg)
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/Image/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}
