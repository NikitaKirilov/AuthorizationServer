package org.example.backend.models.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.info")
public class AppInfoProperties {

    private String version = "0.0.0";
    private String group = "org.example";
    private String name = "backend";
}
