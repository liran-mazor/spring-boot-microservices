package com.books.bookservice.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handle business logic exceptions (capacity reached, etc.)
     * Returns JSON format: {"httpCode": 400, "message": "error details"}
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("httpCode", 400);
        errorResponse.put("message", e.getMessage());
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    /**
     * Handle validation errors (@Valid annotations)
     * Returns JSON format: {"httpCode": 400, "message": "validation details"}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("httpCode", 400);
        
        // Extract first validation error message
        String message = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        errorResponse.put("message", message);
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    /**
     * Handle unexpected server errors
     * Returns JSON format: {"httpCode": 500, "message": "Internal server error"}
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("httpCode", 500);
        errorResponse.put("message", "Internal server error");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}