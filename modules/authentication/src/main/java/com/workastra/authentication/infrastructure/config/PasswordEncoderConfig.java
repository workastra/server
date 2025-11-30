package com.workastra.authentication.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * PasswordEncoderConfig defines password encoding and authentication-related beans.
 *
 * This configuration class sets up:
 * - BCryptPasswordEncoder for secure password hashing
 * - AuthenticationManager for delegating authentication
 * - UserDetailsService with in-memory user store (for demo purposes)
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * Provides the BCryptPasswordEncoder bean for password encryption.
     * BCrypt automatically handles salt generation and secure hashing.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides the AuthenticationManager bean from the AuthenticationConfiguration.
     * This manager delegates to the configured authentication providers.
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Provides an in-memory UserDetailsService with a default admin user.
     * In production, this should be replaced with a persistent user repository
     * connected to a database.
     *
     * Default user:
     * - username: "admin"
     * - password: "admin" (bcrypt encoded)
     * - roles: "USER"
     */
    @Bean
    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        return new InMemoryUserDetailsManager(
            User.withUsername("admin").password(passwordEncoder.encode("admin")).roles("USER").build()
        );
    }
}
