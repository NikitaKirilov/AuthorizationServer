package org.example.backend.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SessionAuthenticationFilter sessionAuthenticationFilter;
    private final SessionRegistry sessionRegistry;

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
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(configurer ->
                        configurer.defaultSuccessUrl(SUCCESS_URL)
                )
                .logout(configurer -> configurer.logoutUrl(LOGOUT_URL))
                .oauth2Login(Customizer.withDefaults())
                .addFilterBefore(sessionAuthenticationFilter, AnonymousAuthenticationFilter.class)
                .sessionManagement(configurer ->
                        configurer.maximumSessions(MAXIMUM_SESSIONS).sessionRegistry(sessionRegistry)
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
