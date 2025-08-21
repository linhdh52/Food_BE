package com.foodbe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcSwaggerRedirect implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/swagger-ui")
                .setViewName("redirect:/swagger-ui/index.html");
        registry.addViewController("/swagger-ui/index.html")
                .setViewName("forward:/webjars/swagger-ui/index.html");
        registry.addViewController("/swagger-ui.html")
                .setViewName("forward:/webjars/swagger-ui/index.html");
    }
}
