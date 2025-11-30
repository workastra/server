package com.workastra.authentication.infrastructure.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

class JsonAccessDeniedHandlerTest {

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void handleWritesForbiddenApiResponse() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Request-Id", "request-456");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        MockHttpServletResponse response = new MockHttpServletResponse();
        JsonAccessDeniedHandler handler = new JsonAccessDeniedHandler(new ObjectMapper());

        handler.handle(request, response, new AccessDeniedException("denied"));

        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
        assertEquals("application/json", response.getContentType());

        Map<String, Object> body = new ObjectMapper().readValue(response.getContentAsString(), MAP_TYPE);
        Object success = body.get("success");
        assertTrue(success instanceof Boolean);
        assertFalse(((Boolean) success).booleanValue());
        assertEquals("FORBIDDEN", body.get("code"));
        assertEquals("You do not have permission to access this resource.", body.get("message"));

        Object metaObj = body.get("meta");
        assertTrue(metaObj instanceof Map);
        Map<?, ?> meta = (Map<?, ?>) metaObj;
        assertEquals("request-456", meta.get("requestId"));
        assertNotNull(meta.get("timestamp"));
    }
}
