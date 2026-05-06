package org.example.backend.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.maxmind.geoip2.DatabaseReader;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import ua_parser.Parser;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
@EnableRedisIndexedHttpSession
@EnableAsync
@EnableResilientMethods
@EnableJpaAuditing
public class AppConfig {

    public static final String PATH_TO_GEOLITE_DB = "/geolite/GeoLite2-City.mmdb";

    @Bean
    public RestOperations restOperations() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return objectMapper;
    }

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return new CookieHttpSessionIdResolver();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }

    @Bean
    public DatabaseReader databaseReader() throws IOException {
        InputStream stream = getClass().getResourceAsStream(PATH_TO_GEOLITE_DB);
        return new DatabaseReader.Builder(stream).build();
    }

    @Bean
    public Parser userAgentParser() {
        return new Parser();
    }
}
