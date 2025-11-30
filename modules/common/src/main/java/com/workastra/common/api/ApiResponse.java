package com.workastra.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * Standardized wrapper for all API responses.
 * <p>
 * This record provides a consistent schema for representing both successful
 * and failed operations, including payload, error details, and technical
 * metadata. It is designed to be version-stable so client applications can
 * reliably perform branching logic based on the response fields.
 *
 * @param success indicates whether the request completed successfully;
 *                {@code true} means success, {@code false} means an error occurred
 * @param code    machine-readable status or error code, stable across versions;
 *                examples: {@code "OK"}, {@code "VALIDATION_ERROR"},
 *                {@code "AUTH_FAILED"}
 * @param message human-readable message for UI or logs; typically {@code null}
 *                for successful responses and populated for user-facing errors
 * @param data    business payload of the response; non-null only when
 *                {@code success == true}
 * @param errors  detailed validation or field-level errors; present when
 *                {@code code == "VALIDATION_ERROR"}, otherwise {@code null}
 * @param meta    technical metadata for the response such as request ID,
 *                timestamp, schema version, and pagination info
 *
 * @param <T>     the type of the business payload returned in {@code data}
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
    boolean success,
    String code,
    @Nullable String message,
    @Nullable T data,
    @Nullable List<ErrorItem> errors,
    Meta meta
) {
    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(true, "OK", null, null, null, Meta.fromCurrentRequest());
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, "OK", null, data, null, Meta.fromCurrentRequest());
    }

    public static <T> ApiResponse<T> error(String code, String message, @Nullable List<ErrorItem> errors, Meta meta) {
        return new ApiResponse<>(false, code, message, null, errors, meta);
    }

    public static <T> ApiResponse<T> error(String code, String message, Meta meta) {
        return error(code, message, null, meta);
    }
}
