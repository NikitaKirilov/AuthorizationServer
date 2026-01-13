package org.example.backend.configs;

import lombok.RequiredArgsConstructor;
import org.example.backend.configs.oauth2.DefaultAuthenticationSuccessHandler;
import org.example.backend.configs.oauth2.FederatedIdentityAuthenticationSuccessHandler;
import org.example.backend.configs.oauth2.OAuth2AuthenticationFailureHandler;
import org.example.backend.configs.security.CustomAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.session.Session;
import org.springframework.session.SingleIndexResolver;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static java.lang.Boolean.TRUE;
import static org.springframework.web.cors.CorsConfiguration.ALL;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String LOGIN_URL = "/login";
    private static final String LOGOUT_URL = "/logout";

    public static final String[] PERMIT_ALL_PATTERN = new String[]{
            "/index.html",
            "/assets/**",
            LOGIN_URL,
            LOGOUT_URL,
            "/oauth2/**",
            "/swagger-ui/**",
            "/app/**"
    };

    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final DefaultAuthenticationSuccessHandler defaultAuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final FederatedIdentityAuthenticationSuccessHandler federatedIdentityAuthenticationSuccessHandler;
    private final SingleIndexResolver<Session> singleIndexResolver;

    @Value("${frontend.base-uri}")
    private String frontendBaseUri; //TODO: move to properties class

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http.authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(PERMIT_ALL_PATTERN).permitAll();
                    authorize.anyRequest().permitAll();
                })
                .sessionManagement(configurer ->
                        //configurer.sessionFixation().changeSessionId(); TODO: enable session id change
                        configurer.withObjectPostProcessor(this.getRedisIndexedSessionRepositoryObjectPostProcessor())
                )
                .rememberMe(AbstractHttpConfigurer::disable)
                .cors(configurer ->
                        configurer.configurationSource(corsConfigurationSource())
                )
                .csrf(csrf -> {
                    csrf.spa();
                    csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
                })
                .formLogin(configurer -> {
                            configurer.loginPage(LOGIN_URL);
                            configurer.failureHandler(customAuthenticationFailureHandler);
                            configurer.successHandler(defaultAuthenticationSuccessHandler);
                        }
                )
                .oauth2Login(configurer -> {
                    configurer.loginPage(LOGIN_URL);
                    configurer.successHandler(federatedIdentityAuthenticationSuccessHandler);
                    configurer.failureHandler(customAuthenticationFailureHandler);
                    configurer.withObjectPostProcessor(this.getRequestRedirectFilterObjectPostProcessor());
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
        corsConfiguration.setExposedHeaders(List.of("redirect"));

        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return corsConfigurationSource;
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    private ObjectPostProcessor<OAuth2AuthorizationRequestRedirectFilter> getRequestRedirectFilterObjectPostProcessor() {
        return new ObjectPostProcessor<>() {
            @Override
            public <O extends OAuth2AuthorizationRequestRedirectFilter> O postProcess(O object) {
                object.setAuthenticationFailureHandler(oAuth2AuthenticationFailureHandler);
                return object;
            }
        };
    }

    private ObjectPostProcessor<RedisIndexedSessionRepository> getRedisIndexedSessionRepositoryObjectPostProcessor() {
        return new ObjectPostProcessor<>() {
            @Override
            public <O extends RedisIndexedSessionRepository> O postProcess(O object) {
                object.setIndexResolver(singleIndexResolver);
                return object;
            }
        };
    }
}
