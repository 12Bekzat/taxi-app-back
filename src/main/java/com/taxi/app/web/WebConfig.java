package com.taxi.app.web;

// src/main/java/kz/redtaxi/config/WebConfig.java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.base-path}")
    private String uploadBasePath;

    @Value("${app.upload.base-url}")
    private String uploadBaseUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Пример: base-path = "uploads", base-url = "/files"
        // => URL: /files/** -> file:uploads/
        String normalizedPath = uploadBasePath.endsWith("/")
                ? uploadBasePath
                : uploadBasePath + "/";

        String location = "file:" + normalizedPath;

        registry
                .addResourceHandler(uploadBaseUrl + "/**")
                .addResourceLocations(location);
    }
}
