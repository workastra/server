package com.workastra.authentication.infrastructure.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import tools.jackson.databind.ObjectMapper;

/**
 * JsonAuthFailureHandler handles authentication failures by returning
 * a JSON response with error details.
 */
public class JsonAuthFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    public JsonAuthFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException ex
    ) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        var body = Map.of("success", false, "error", ex.getMessage());

        response.getWriter().write(this.objectMapper.writeValueAsString(body));
    }
}
