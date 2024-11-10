package org.example.backend.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
@EnableRedisIndexedHttpSession(maxInactiveIntervalInSeconds = 31536000) //TODO move to properties
public class AppConfig {

    @Bean
    public RestOperations restOperations() {
        return new RestTemplate();
    }
}
