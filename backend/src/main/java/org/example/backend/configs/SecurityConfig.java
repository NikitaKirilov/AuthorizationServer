package org.example.backend.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] PERMIT_ALL = new String[]{
            "/login/**",
            "/oauth2/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                        authorize -> {
                            authorize.requestMatchers(PERMIT_ALL).permitAll();
                            authorize.anyRequest().authenticated();
                        }
                )
                .formLogin(withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //TODO: keep in memory 1 admin user that will be created with username and password from application.properties
    /*
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.builder()
                .username("admin") //default value
                .password("admin") //default value
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }
    */
}
