package org.example.backend.models.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {

    private boolean allowCredentials = false;
    private List<String> allowedHeaders;
    private List<String> allowedOrigins;
    private List<String> allowedMethods = List.of(HttpMethod.GET.name(), HttpMethod.HEAD.name());
    private List<String> exposedHeaders;
    private String pathPattern = "/**";
}
