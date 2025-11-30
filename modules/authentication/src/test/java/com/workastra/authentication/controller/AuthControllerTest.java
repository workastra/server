package com.workastra.authentication.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.workastra.common.api.ApiResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

class AuthControllerTest {

    private final AuthController controller = new AuthController();
    private MockHttpServletRequest request;

    @BeforeEach
    void setUpRequestContext() {
        request = new MockHttpServletRequest();
        request.addHeader("X-Request-Id", "test-request");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @AfterEach
    void clearRequestContext() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void getCsrfWrapsTokenInApiResponse() {
        CsrfToken csrfToken = new DefaultCsrfToken("X-CSRF", "_csrf", "token-123");

        ApiResponse<CsrfToken> response = controller.getCsrf(csrfToken);

        assertTrue(response.success());
        assertEquals("OK", response.code());
        assertNull(response.message());
        assertSame(csrfToken, response.data());
        assertNull(response.errors());
        assertNotNull(response.meta());
    }

    @Test
    void meReturnsAuthenticatedPrincipal() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password");

        ApiResponse<Authentication> response = controller.me(authentication);

        assertTrue(response.success());
        assertEquals("OK", response.code());
        assertSame(authentication, response.data());
    }
}
