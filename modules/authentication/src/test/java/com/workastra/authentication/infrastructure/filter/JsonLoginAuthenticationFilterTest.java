package com.workastra.authentication.infrastructure.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.mockito.Mockito;
import tools.jackson.databind.ObjectMapper;

class JsonLoginAuthenticationFilterTest {

    private AuthenticationManager authenticationManager;

    private JsonLoginAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        authenticationManager = Mockito.mock(AuthenticationManager.class);
        Assert.notNull(authenticationManager, "authenticationManager");
        filter = new JsonLoginAuthenticationFilter(authenticationManager, new ObjectMapper());
    }

    @Test
    void attemptAuthenticationDelegatesToManagerWhenJsonPayloadIsValid() {
        Authentication authenticated = new TestingAuthenticationToken("alice", "password");
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authenticated);

        MockHttpServletRequest request = new MockHttpServletRequest(HttpMethod.POST.name(), "/api/v1/auth/login");
        request.setContent("{\"username\":\"alice\",\"password\":\"secret\"}".getBytes(StandardCharsets.UTF_8));
        MockHttpServletResponse response = new MockHttpServletResponse();

        Authentication result = filter.attemptAuthentication(request, response);

        verify(authenticationManager).authenticate(
            argThat(auth -> {
                assertTrue(auth instanceof UsernamePasswordAuthenticationToken);
                assertEquals("alice", auth.getPrincipal());
                assertEquals("secret", auth.getCredentials());
                return true;
            })
        );
        assertEquals(authenticated, result);
    }

    @Test
    void attemptAuthenticationRejectsNonPostMethods() {
        MockHttpServletRequest request = new MockHttpServletRequest(HttpMethod.GET.name(), "/api/v1/auth/login");
        MockHttpServletResponse response = new MockHttpServletResponse();

        AuthenticationServiceException exception = assertThrows(
            AuthenticationServiceException.class,
            () -> filter.attemptAuthentication(request, response)
        );

        assertEquals("Authentication method not supported: GET", exception.getMessage());
    }

    @Test
    void attemptAuthenticationRejectsInvalidJson() {
        MockHttpServletRequest request = new MockHttpServletRequest(HttpMethod.POST.name(), "/api/v1/auth/login");
        request.setContent("not-a-json".getBytes(StandardCharsets.UTF_8));
        MockHttpServletResponse response = new MockHttpServletResponse();

        BadCredentialsException exception = assertThrows(
            BadCredentialsException.class,
            () -> filter.attemptAuthentication(request, response)
        );

        assertEquals("Invalid login request", exception.getMessage());
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }
}
