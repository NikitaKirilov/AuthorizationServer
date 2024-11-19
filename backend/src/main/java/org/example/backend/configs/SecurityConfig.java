package org.example.backend.configs;

import lombok.RequiredArgsConstructor;
import org.example.backend.configs.security.OAuth2AuthenticationFailureHandler;
import org.example.backend.configs.security.OAuth2LoginAuthenticationSuccessHandler;
import org.example.backend.configs.security.RedisPrincipalNameIndexResolver;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static java.lang.Boolean.TRUE;
import static org.springframework.web.cors.CorsConfiguration.ALL;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final ObjectPostProcessor<OAuth2AuthorizationRequestRedirectFilter> REQUEST_REDIRECT_FILTER_OBJECT_POST_PROCESSOR = OAuth2AuthenticationFailureHandler.getPostProcessor();
    private static final ObjectPostProcessor<RedisIndexedSessionRepository> SESSION_REPOSITORY_OBJECT_POST_PROCESSOR = RedisPrincipalNameIndexResolver.getPostProcessor();

    private static final int MAXIMUM_SESSIONS = 1;

    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_URL = "/logout";

    private static final String[] PERMIT_ALL_PATTERN = new String[]{
            "/index.html",
            "/assets/**",
            LOGIN_URL,
            LOGOUT_URL,
            "/oauth2/**",
            "/swagger-ui/**",
            "/idp-registrations/**",
    };

    private final OAuth2LoginAuthenticationSuccessHandler oAuth2LoginAuthenticationSuccessHandler;
    private final RememberMeServices rememberMeServices;

    @Value("${frontend.base-uri}")
    private String frontendBaseUri; //TODO: move to properties class

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(PERMIT_ALL_PATTERN).permitAll();
                    authorize.anyRequest().authenticated();
                })
                .sessionManagement(configurer -> {
                    configurer.maximumSessions(MAXIMUM_SESSIONS);
                    configurer.withObjectPostProcessor(SESSION_REPOSITORY_OBJECT_POST_PROCESSOR);
                })
                .rememberMe(configurer -> {
                            configurer.alwaysRemember(TRUE);
                            configurer.rememberMeServices(rememberMeServices);
                        }
                )
                .cors(configurer ->
                        configurer.configurationSource(corsConfigurationSource())
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(configurer ->
                        configurer.loginPage(LOGIN_URL) //TODO: create AuthenticationFailureHandler
                )
                .oauth2Login(configurer -> {
                    configurer.loginPage(LOGIN_URL);
                    configurer.successHandler(oAuth2LoginAuthenticationSuccessHandler);
                    configurer.withObjectPostProcessor(REQUEST_REDIRECT_FILTER_OBJECT_POST_PROCESSOR);
                })
                .logout(configurer ->
                        configurer.logoutUrl(LOGOUT_URL)
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() { //TODO: make this bean configurable
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(TRUE);
        corsConfiguration.addAllowedOrigin(frontendBaseUri);
        corsConfiguration.addAllowedHeader(ALL);
        corsConfiguration.addAllowedMethod(ALL);

        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return corsConfigurationSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
