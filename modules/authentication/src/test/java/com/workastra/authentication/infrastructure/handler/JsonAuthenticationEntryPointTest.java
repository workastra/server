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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

class JsonAuthenticationEntryPointTest {

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void commenceWritesUnauthorizedApiResponse() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Request-Id", "request-123");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        MockHttpServletResponse response = new MockHttpServletResponse();
        JsonAuthenticationEntryPoint entryPoint = new JsonAuthenticationEntryPoint(new ObjectMapper());

        entryPoint.commence(request, response, new BadCredentialsException("bad"));

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("application/json", response.getContentType());

        Map<String, Object> body = new ObjectMapper().readValue(response.getContentAsString(), MAP_TYPE);
        Object success = body.get("success");
        assertTrue(success instanceof Boolean);
        assertFalse(((Boolean) success).booleanValue());
        assertEquals("UNAUTHORIZED", body.get("code"));
        assertEquals("Authentication is required to access this resource.", body.get("message"));

        Object metaObj = body.get("meta");
        assertTrue(metaObj instanceof Map);
        Map<?, ?> meta = (Map<?, ?>) metaObj;
        assertEquals("request-123", meta.get("requestId"));
        assertNotNull(meta.get("timestamp"));
    }
}
