package com.workastra.authentication;

import com.workastra.authentication.infrastructure.handler.JsonAccessDeniedHandler;
import com.workastra.authentication.infrastructure.handler.JsonAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

/**
 * AuthenticationModuleConfig is the module-level bootstrap configuration for the authentication module.
 *
 * This configuration is responsible for:
 * - Registering exception handlers as Spring components
 * - Providing infrastructure beans needed across the module
 *
 * Actual security configuration is delegated to the infrastructure layer:
 * - SecurityConfig: HTTP security filter chain and authorization rules
 * - PasswordEncoderConfig: Password encoding and authentication beans
 * - Various handlers: JSON-based exception and authentication event handling
 */
@Configuration
public class AuthenticationModuleConfig {

    /**
     * Registers the JsonAuthenticationEntryPoint as a Spring component.
     * This handler processes 401 Unauthorized responses.
     */
    @Bean
    JsonAuthenticationEntryPoint jsonAuthenticationEntryPoint(ObjectMapper mapper) {
        return new JsonAuthenticationEntryPoint(mapper);
    }

    /**
     * Registers the JsonAccessDeniedHandler as a Spring component.
     * This handler processes 403 Forbidden responses.
     */
    @Bean
    JsonAccessDeniedHandler jsonAccessDeniedHandler(ObjectMapper mapper) {
        return new JsonAccessDeniedHandler(mapper);
    }
}
