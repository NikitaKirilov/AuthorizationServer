package org.example.backend.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final ObjectPostProcessor<OAuth2AuthorizationRequestRedirectFilter> REQUEST_REDIRECT_FILTER_OBJECT_POST_PROCESSOR = OAuth2AuthenticationFailureHandler.getPostProcessor();
    private static final ObjectPostProcessor<RedisIndexedSessionRepository> SESSION_REPOSITORY_OBJECT_POST_PROCESSOR = RedisPrincipalNameIndexResolver.getPostProcessor();

    private static final int MAXIMUM_SESSIONS = 1;

    private static final String SUCCESS_URL = "/success";
    private static final String LOGOUT_URL = "/logout";

    private static final String[] PERMIT_ALL_PATTERN = new String[]{
            LOGOUT_URL,
            "/login",
            "/oauth2/**",
            "/swagger-ui/**",
    };

    private final OAuth2LoginAuthenticationSuccessHandler oAuth2LoginAuthenticationSuccessHandler;
    private final RememberMeServices rememberMeServices;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(PERMIT_ALL_PATTERN).permitAll();
                    authorize.anyRequest().permitAll(); //TODO: authenticated
                })
                .sessionManagement(configurer -> {
                    configurer.maximumSessions(MAXIMUM_SESSIONS);
                    configurer.withObjectPostProcessor(SESSION_REPOSITORY_OBJECT_POST_PROCESSOR);
                })
                .rememberMe(configurer ->
                        configurer.rememberMeServices(rememberMeServices)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(configurer ->
                        configurer.defaultSuccessUrl(SUCCESS_URL)
                )
                .logout(configurer ->
                        configurer.logoutUrl(LOGOUT_URL)
                )
                .oauth2Login(configurer -> {
                    configurer.successHandler(oAuth2LoginAuthenticationSuccessHandler);
                    configurer.withObjectPostProcessor(REQUEST_REDIRECT_FILTER_OBJECT_POST_PROCESSOR);
                })
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
