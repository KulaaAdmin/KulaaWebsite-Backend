package com.kula.kula_project_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * WebMvcConfig is a configuration class that sets up web MVC settings for the application.
 * It implements the WebMvcConfigurer interface which provides default methods for customization of
 * Spring MVC's configuration.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * Configures resource handlers for serving static resources.
     * @param registry The ResourceHandlerRegistry instance.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/images/**").addResourceLocations("file:/Users/jionghaochen/Downloads/images/");
        registry.addResourceHandler("/images/**").addResourceLocations("file:/root/images/");


    }
    /**
     * Configures Cross-Origin Resource Sharing (CORS) settings.
     * @param registry The CorsRegistry instance.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allows CORS requests to all endpoints from any origin.
        // Allows all HTTP methods and headers.
        // Allows credentials to be included in CORS requests.
        // Sets the maximum age of the CORS preflight request to 1 hour.
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .allowedOriginPatterns("*")
                //.allowedOrigins("*")
                .allowedOrigins("http://localhost:5173")
                .maxAge(3600);
    }
}
