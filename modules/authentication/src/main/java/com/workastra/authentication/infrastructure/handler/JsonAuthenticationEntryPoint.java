package com.workastra.authentication.infrastructure.handler;

import com.workastra.common.api.ApiResponse;
import com.workastra.common.api.Meta;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import tools.jackson.databind.ObjectMapper;

/**
 * JsonAuthenticationEntryPoint handles unauthorized access by returning
 * a JSON response with a 401 status code.
 */
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    public JsonAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<Void> body = ApiResponse.error(
            "UNAUTHORIZED",
            "Authentication is required to access this resource.",
            Meta.fromCurrentRequest()
        );

        response.getWriter().write(mapper.writeValueAsString(body));
    }
}
