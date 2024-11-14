package org.example.backend.models.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static org.apache.logging.log4j.util.Strings.EMPTY;

@Data
@Component
@ConfigurationProperties(prefix = "remember-me")
public class RememberMeProperties {

    private String key = EMPTY;
    private boolean alwaysRemember = true;
    private boolean createTableOnStartup = true;
    private boolean useSecureCookies = true;
    private int tokenValiditySeconds = 31536000;
}
