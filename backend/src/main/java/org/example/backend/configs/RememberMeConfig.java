package org.example.backend.configs;

import lombok.RequiredArgsConstructor;
import org.example.backend.models.properties.RememberMeProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class RememberMeConfig {

    private final DataSource dataSource;
    private final RememberMeProperties rememberMeProperties;
    private final UserDetailsService userDetailsService;

    @Bean
    public RememberMeServices rememberMeServices() {
        PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices(
                rememberMeProperties.getKey(),
                userDetailsService,
                persistentTokenRepository()
        );

        rememberMeServices.setAlwaysRemember(rememberMeProperties.isAlwaysRemember());
        rememberMeServices.setUseSecureCookie(rememberMeProperties.isUseSecureCookies());
        rememberMeServices.setTokenValiditySeconds(rememberMeProperties.getTokenValiditySeconds());

        return rememberMeServices;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();

        tokenRepository.setDataSource(dataSource);
        tokenRepository.setCreateTableOnStartup(rememberMeProperties.isCreateTableOnStartup());

        return tokenRepository;
    }
}
