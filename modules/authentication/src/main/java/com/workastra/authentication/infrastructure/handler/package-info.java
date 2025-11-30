/**
 * Infrastructure layer handlers for security exceptions and authentication events.
 *
 * This package contains Spring Security handlers that process authentication
 * and authorization outcomes, returning standardized JSON responses.
 *
 * - JsonAuthenticationEntryPoint: Handles 401 Unauthorized responses
 * - JsonAccessDeniedHandler: Handles 403 Forbidden responses
 * - JsonAuthSuccessHandler: Handles successful authentication
 * - JsonAuthFailureHandler: Handles authentication failures
 */
package com.workastra.authentication.infrastructure.handler;
