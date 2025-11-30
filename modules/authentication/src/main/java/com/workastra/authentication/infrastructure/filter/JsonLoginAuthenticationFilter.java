package com.workastra.authentication.infrastructure.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

/**
 * JsonLoginAuthenticationFilter is a custom authentication filter that processes
 * login requests containing username and password in JSON format. It extends
 * the UsernamePasswordAuthenticationFilter to handle authentication via a JSON
 * payload instead of traditional form data.
 *
 * This filter expects a POST request with a JSON body that includes the following
 * fields:
 * - username: The username of the user attempting to authenticate.
 * - password: The password of the user attempting to authenticate.
 *
 * If the request method is not POST, an AuthenticationServiceException is thrown.
 * Upon successful authentication, it returns an Authentication object; otherwise,
 * it returns null in case of an IOException during the reading of the request input.
 */
public class JsonLoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    record LoginRequest(String username, String password) {}

    private final ObjectMapper objectMapper;

    public JsonLoginAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        super(authenticationManager);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException {
        if (!request.getMethod().equals(HttpMethod.POST.name())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        try {
            var loginRequest = this.objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            var authentication = UsernamePasswordAuthenticationToken.unauthenticated(
                loginRequest.username(),
                loginRequest.password()
            );
            setDetails(request, authentication);
            return getAuthenticationManager().authenticate(authentication);
        } catch (JacksonException | IOException e) {
            throw new BadCredentialsException("Invalid login request", e);
        }
    }
}
