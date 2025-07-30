package com.books.recommendedservice.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Handle business logic exceptions (capacity reached, etc.)
 * Returns JSON format: {"httpCode": 400, "message": "error details"}
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles RuntimeException (including BookService communication errors)
     * Returns JSON error response matching the required format
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleRuntimeException(RuntimeException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("httpCode", 500);
        errorResponse.put("message", e.getMessage());
        return errorResponse;
    }

    /**
     * Handles any other unexpected exceptions
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleGenericException(Exception e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("httpCode", 500);
        errorResponse.put("message", "An unexpected error occurred");
        return errorResponse;
    }
}