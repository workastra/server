package com.workastra.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Meta(
    String requestId,
    String timestamp, // ISO-8601
    @Nullable Pagination pagination
) {
    public static Meta fromCurrentRequest() {
        HttpServletRequest req = currentRequest();

        String requestId = resolveRequestId(req);
        String timestamp = Instant.now().toString(); // always ISO-8601 UTC
        // Pagination pagination = resolvePagination(req);

        return new Meta(requestId, timestamp, null);
    }

    private static HttpServletRequest currentRequest() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attr == null) {
            throw new IllegalStateException(
                "No current HTTP request — Meta.fromCurrentRequest() called outside web context"
            );
        }

        return attr.getRequest();
    }

    private static String resolveRequestId(HttpServletRequest req) {
        // 1. Prefer header: X-Request-Id
        String id = req.getHeader("X-Request-Id");
        if (id != null && !id.isBlank()) {
            return id;
        }

        // 2. Try Slf4j MDC (used by many log systems)
        id = org.slf4j.MDC.get("requestId");
        if (id != null && !id.isBlank()) {
            return id;
        }

        // 3. Generate a new one
        return UUID.randomUUID().toString();
    }

    // private static Pagination resolvePagination(HttpServletRequest req) {
    //     String page = req.getParameter("page");
    //     String size = req.getParameter("size");

    //     if (page == null || size == null) return null;

    //     try {
    //         return new Pagination(
    //                 Integer.parseInt(page),
    //                 Integer.parseInt(size)
    //         );
    //     } catch (NumberFormatException ex) {
    //         return null;
    //     }
    // }
}
