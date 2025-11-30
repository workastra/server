package com.workastra.authentication.infrastructure.handler;

import com.workastra.common.api.ApiResponse;
import com.workastra.common.api.Meta;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import tools.jackson.databind.ObjectMapper;

/**
 * JsonAccessDeniedHandler handles access denied (forbidden) access by returning
 * a JSON response with a 403 status code.
 */
public class JsonAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper;

    public JsonAccessDeniedHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response
            .getWriter()
            .write(
                this.mapper.writeValueAsString(
                    ApiResponse.error(
                        "FORBIDDEN",
                        "You do not have permission to access this resource.",
                        Meta.fromCurrentRequest()
                    )
                )
            );
    }
}
