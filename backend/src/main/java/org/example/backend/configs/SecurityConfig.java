package org.example.backend.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2RedirectFilterPostProcessor oAuth2RedirectFilterPostProcessor = new OAuth2RedirectFilterPostProcessor();

    private static final String[] PERMIT_ALL_PATTERN = new String[]{
            "login",
            "/oauth2/**"
    };

    private static final int MAXIMUM_SESSIONS = 1;

    private static final String SUCCESS_URL = "/success";
    private static final String LOGOUT_URL = "/logout";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                        authorize -> {
                            authorize.requestMatchers(PERMIT_ALL_PATTERN).permitAll();
                            authorize.anyRequest().permitAll(); //TODO: authenticated
                        }
                )
                .sessionManagement(configurer ->
                        configurer.maximumSessions(MAXIMUM_SESSIONS)
                )

                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(configurer ->
                        configurer.defaultSuccessUrl(SUCCESS_URL)
                )
                .logout(configurer -> configurer.logoutUrl(LOGOUT_URL))
                .oauth2Login(configurer ->
                        configurer.withObjectPostProcessor(oAuth2RedirectFilterPostProcessor)
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
