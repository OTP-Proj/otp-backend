package org.otp.otp.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowedOriginPatterns("*", "http://localhost:3004", "localhost:3004", "localhost",
                        "http://10.100.40.14", "10.100.40.14", "http://81.17.88.212:8899",
                        "81.17.88.212")
                .allowCredentials(true);
    }
}
