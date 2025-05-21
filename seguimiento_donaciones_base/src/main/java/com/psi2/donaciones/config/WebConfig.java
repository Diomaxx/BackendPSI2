package com.psi2.donaciones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permite solicitudes CORS desde todos los orígenes
        registry.addMapping("/**")  // Aplicamos CORS a todos los endpoints
                .allowedOrigins("*")  // Permite solicitudes desde cualquier origen
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")  // Métodos permitidos
                .allowedHeaders("Content-Type", "Authorization", "Accept", "Origin", "X-Requested-With");  // Encabezados permitidos
    }
}
