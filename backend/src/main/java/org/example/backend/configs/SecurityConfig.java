package org.example.backend.configs;

import lombok.RequiredArgsConstructor;
import org.example.backend.configs.oauth2.OAuth2AuthenticationFailureHandler;
import org.example.backend.configs.security.CustomAuthenticationFailureHandler;
import org.example.backend.configs.security.DefaultAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.session.Session;
import org.springframework.session.SingleIndexResolver;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String LOGIN_PAGE = "/app/login";
    private static final String LOGOUT_URL = "/logout";

    private static final String[] PERMIT_ALL_PATTERN = new String[]{
            "/index.html",
            "/assets/**",
            "/login",
            LOGIN_PAGE,
            LOGOUT_URL,
            "/swagger-ui/**",
            "/app/**",
            "/registrations/**",
            "/error**",
            "/.well-known/appspecific/com.chrome.devtools.json" //TODO: remove later
    };

    private final CorsConfigurationSource defaultCorsConfigurationSource;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final DefaultAuthenticationSuccessHandler defaultAuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final SingleIndexResolver<Session> singleIndexResolver;

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http.authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(PERMIT_ALL_PATTERN).permitAll();
                    authorize.anyRequest().authenticated();
                })
                .sessionManagement(configurer ->
                        //configurer.sessionFixation().changeSessionId(); TODO: enable session id change
                        configurer.withObjectPostProcessor(this.getRedisIndexedSessionRepositoryObjectPostProcessor())
                )
                .rememberMe(AbstractHttpConfigurer::disable)
                .cors(configurer ->
                        configurer.configurationSource(defaultCorsConfigurationSource)
                )
                .csrf(csrf -> {
                    csrf.spa();
                    csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
                })
                .formLogin(configurer ->
                        configurer.loginPage(LOGIN_PAGE)
                                .loginProcessingUrl("/login")
                                .failureHandler(customAuthenticationFailureHandler)
                                .successHandler(defaultAuthenticationSuccessHandler)
                )
                .oauth2Login(configurer -> {
                    configurer.loginPage(LOGIN_PAGE);
                    configurer.withObjectPostProcessor(this.getRequestRedirectFilterObjectPostProcessor());
                })
                .logout(configurer ->
                        configurer.logoutUrl(LOGOUT_URL)
                )
                .build();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
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
