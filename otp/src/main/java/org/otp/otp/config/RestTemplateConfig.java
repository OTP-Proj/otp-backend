package org.otp.otp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .requestFactory(this::clientHttpRequestFactory)
                .additionalInterceptors(customClientHttpRequestInterceptor())
                .build();
    }

    private ClientHttpRequestInterceptor customClientHttpRequestInterceptor() {
        return (request, body, execution) -> {
            log.info("Calling URI: {}", request.getURI());
            return execution.execute(request, body);
        };
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        return new SimpleClientHttpRequestFactory();
    }
}
