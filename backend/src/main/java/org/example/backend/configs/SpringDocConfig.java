package org.example.backend.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.example.backend.models.properties.AppInfoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SpringDocConfig {

    private static final String TITLE = "Spring Authorization Server OpenAPI";
    private static final String DESCRIPTION = "OpenAPI documentation of Spring Authorization Server backend module endpoints";

    private final AppInfoProperties appInfoProperties;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(TITLE)
                        .description(DESCRIPTION)
                        .version(appInfoProperties.getVersion())
                );
    }
}
