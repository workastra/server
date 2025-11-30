package com.workastra.authentication.controller;

import com.workastra.common.api.ApiResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping(path = "/api/v{version}/csrf", version = "1")
    public ApiResponse<CsrfToken> getCsrf(CsrfToken csrfToken) {
        return ApiResponse.ok(csrfToken);
    }

    @GetMapping(path = "/api/v{version}/auth/me", version = "1")
    public ApiResponse<Authentication> me(Authentication authentication) {
        return ApiResponse.ok(authentication);
    }
}
