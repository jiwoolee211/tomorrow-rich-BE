package com.likelion.tomorrowrich.global.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        int statusCode,
        String error,
        String message,
        String details,
        String errorCode
) {

    public static ErrorResponse of(ErrorCode errorCode, String details) {
        return new ErrorResponse(
                LocalDateTime.now(),
                errorCode.getStatus().value(),
                errorCode.getStatus().getReasonPhrase(),
                errorCode.getMessage(),
                details,
                errorCode.getCode()
        );
    }
}
