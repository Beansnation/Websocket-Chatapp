package com.david.chatapp.config;

import com.david.chatapp.filter.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

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
                // Disables CSRF (Cross-Site Request Forgery) protection, typically used in stateless applications like APIs
                .csrf(AbstractHttpConfigurer::disable)

                // Allows whitelisted endpoints to be accessed without authentication
                .authorizeHttpRequests(req -> req
                        // Permit all requests to authentication endpoints and static resources (e.g., for UI)
                        .requestMatchers("/api/v1/auth/**", "/ws-chat/**", "/index.html").permitAll()

                        // Require authentication for all other endpoints
                        .anyRequest().authenticated()
                )

                // Configures session management to be stateless, meaning no session will be created
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Adds a custom JWT authentication filter before the UsernamePasswordAuthenticationFilter
                .authenticationProvider(authenticationProvider) // Sets up the custom authentication provider
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JWT filter to be executed before standard authentication to verify registered users
        return http.build();
    }
}