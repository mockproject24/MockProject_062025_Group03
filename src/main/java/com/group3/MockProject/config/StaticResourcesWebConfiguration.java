package com.group3.MockProject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * StaticResourcesWebConfiguration
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 04/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 04/07/2025   Hải Đăng      Create
 */
@Configuration
public class StaticResourcesWebConfiguration implements WebMvcConfigurer {

    @Value("${spring.upload-file.base-uri}")
    private String baseURI;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/storage/**")
                .addResourceLocations(baseURI);
    }
}
