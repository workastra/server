package com.workastra.authentication.infrastructure.config;

import com.workastra.authentication.infrastructure.filter.JsonLoginAuthenticationFilter;
import com.workastra.authentication.infrastructure.handler.JsonAccessDeniedHandler;
import com.workastra.authentication.infrastructure.handler.JsonAuthFailureHandler;
import com.workastra.authentication.infrastructure.handler.JsonAuthSuccessHandler;
import com.workastra.authentication.infrastructure.handler.JsonAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import tools.jackson.databind.ObjectMapper;

/**
 * SecurityConfig configures Spring Security for the authentication module.
 * It sets up the HTTP security chain, CSRF protection, authorization rules,
 * and integrates custom JSON-based authentication handlers.
 */
@Configuration
public class SecurityConfig {

    private final JsonAccessDeniedHandler accessDeniedHandler;
    private final JsonAuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(
        JsonAuthenticationEntryPoint authenticationEntryPoint,
        JsonAccessDeniedHandler accessDeniedHandler
    ) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * - Enables CSRF protection for Single Page Applications (SPA)
     * - Permits public endpoints: /actuator/health, /api/v1/csrf, /api/v1/auth/login
     * - Requires authentication for all other /api/** endpoints
     * - Denies all other requests
     * - Uses custom JSON authentication filter instead of form login
     * - Registers custom exception handlers for unauthorized and forbidden access
     */
    @Bean
    SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        AuthenticationManager authenticationManager,
        ObjectMapper objectMapper
    ) throws Exception {
        var jsonLoginAuthenticationFilter = new JsonLoginAuthenticationFilter(authenticationManager, objectMapper);
        jsonLoginAuthenticationFilter.setFilterProcessesUrl("/api/v1/auth/login");
        jsonLoginAuthenticationFilter.setAuthenticationSuccessHandler(new JsonAuthSuccessHandler(objectMapper));
        jsonLoginAuthenticationFilter.setAuthenticationFailureHandler(new JsonAuthFailureHandler(objectMapper));

        var sessionRepo = new HttpSessionSecurityContextRepository();
        jsonLoginAuthenticationFilter.setSecurityContextRepository(sessionRepo);

        http
            .csrf((csrf) -> csrf.spa())
            .authorizeHttpRequests((auth) ->
                auth
                    .requestMatchers(HttpMethod.GET, "/actuator/health")
                    .permitAll()
                    .requestMatchers("/api/v1/csrf")
                    .permitAll()
                    .requestMatchers("/api/v1/auth/login")
                    .permitAll()
                    .requestMatchers("/api/**")
                    .authenticated()
                    .anyRequest()
                    .denyAll()
            )
            .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .formLogin((c) -> c.disable())
            .httpBasic((c) -> c.disable())
            .addFilterAt(jsonLoginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling((ex) -> {
                ex.authenticationEntryPoint(this.authenticationEntryPoint);
                ex.accessDeniedHandler(this.accessDeniedHandler);
            });

        return http.build();
    }
}
