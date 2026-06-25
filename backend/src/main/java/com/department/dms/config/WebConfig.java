package com.department.dms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/faculty/**")
                .addResourceLocations("file:/C:/Users/dshre/OneDrive/Documents/3RDYEA~1/PROJEC~1/FACULT~1/faculty/");
    }
}
