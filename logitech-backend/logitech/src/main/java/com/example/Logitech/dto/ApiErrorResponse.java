package com.example.Logitech.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        List<String> messages
) {
    public static ApiErrorResponse of(int status, String error, String message) {
        return new ApiErrorResponse(LocalDateTime.now(), status, error, List.of(message));
    }

    public static ApiErrorResponse of(int status, String error, List<String> messages) {
        return new ApiErrorResponse(LocalDateTime.now(), status, error, messages);
    }
}
