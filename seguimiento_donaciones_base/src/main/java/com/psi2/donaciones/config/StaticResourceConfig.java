package com.psi2.donaciones.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configurar para que Spring Boot sirva imágenes desde el directorio externo
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:/C:/temp/imagenesDonacion/");  // Ruta absoluta del directorio externo
    }
}
