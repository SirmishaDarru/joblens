package com.joblens.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
@Configuration public class WebConfig implements WebMvcConfigurer {
    @Value("${joblens.frontend-origin:http://localhost:5173}") private String frontendOrigin;
    @Override public void addCorsMappings(CorsRegistry r){r.addMapping("/api/**").allowedOrigins(frontendOrigin,"http://localhost:5173").allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS").allowedHeaders("*");}
}
