package org.example.backend.configs;

import lombok.RequiredArgsConstructor;
import org.example.backend.configs.oauth2.OAuth2AuthenticationFailureHandler;
import org.example.backend.configs.security.CustomAuthenticationFailureHandler;
import org.example.backend.configs.security.DefaultAuthenticationEntryPoint;
import org.example.backend.configs.security.EmailVerifiedFilter;
import org.example.backend.configs.security.LoginAttemptFilter;
import org.example.backend.configs.security.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String LOGIN_PROCESSING_URL = "/login";
    public static final String LOGIN_PAGE = "/app/login";
    private static final String LOGOUT_URL = "/logout";

    private static final String[] PERMIT_ALL_PATTERN = new String[]{
            "/csrf",
            "/index.html",
            "/assets/**",
            LOGIN_PROCESSING_URL,
            LOGIN_PAGE,
            LOGOUT_URL,
            "/swagger-ui/**",
            "/app/**",
            "/registrations/**",
            "/error**",
            "/.well-known/appspecific/com.chrome.devtools.json" //TODO: remove later
    };

    public static final List<PathPatternRequestMatcher> PERMIT_ALL_LIST = Arrays.stream(PERMIT_ALL_PATTERN)
            .map(PathPatternRequestMatcher::pathPattern)
            .toList();

    private final CorsConfigurationSource defaultCorsConfigurationSource;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final DefaultAuthenticationEntryPoint authenticationEntryPoint;
    private final EmailVerifiedFilter emailVerifiedFilter;
    private final LoginAttemptFilter loginAttemptFilter;
    private final LoginSuccessHandler loginSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http.authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(PERMIT_ALL_PATTERN).permitAll();
                    authorize.anyRequest().authenticated();
                })
                .sessionManagement(configurer -> {
                    configurer.maximumSessions(5);
                    configurer.sessionFixation().changeSessionId();
                })
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
                                .loginProcessingUrl(LOGIN_PROCESSING_URL)
                                .failureHandler(customAuthenticationFailureHandler)
                                .successHandler(loginSuccessHandler)
                )
                .exceptionHandling(customizer ->
                        customizer.authenticationEntryPoint(authenticationEntryPoint)
                )
                .oauth2Login(configurer -> {
                    configurer.loginPage(LOGIN_PAGE);
                    configurer.withObjectPostProcessor(this.getRequestRedirectFilterObjectPostProcessor());
                })
                .logout(configurer ->
                        configurer.logoutUrl(LOGOUT_URL)
                )
                .addFilterBefore(loginAttemptFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(emailVerifiedFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
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
}
