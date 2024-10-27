package com.david.chatapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {


    /**
     * Configures the Spring Security filter chain.
     * This method sets up key security parts like CSRF disabling, whitelisting authentication-related
     * endpoints, setting stateless session management, and adding a JWT authentication filter before the
     * UsernamePasswordAuthenticationFilter in the filter chain.
     *
     * @param http - the HttpSecurity object that allows configuring web-based security for specific HTTP requests
     * @return SecurityFilterChain - the built security filter chain to be used by the application
     * @throws Exception - in case of any configuration errors
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                // Allows whitelisted endpoints to be accessed without authentication
                .authorizeHttpRequests(req -> req
                        // Permit all requests to authentication endpoints and static resources (e.g., for UI)
                        .requestMatchers("/api/v1/auth/**", "/ws-chat/**", "/index.html").permitAll()

                        // Require authentication for all other endpoints
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}