package ru.diplom.diplom.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Для всех эндпоинтов
                .allowedOrigins("*")  // Разрешить доступ из любого источника
                .allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS")  // Разрешенные методы
                .allowedHeaders("*");  // Разрешенные заголовки
    }
}

