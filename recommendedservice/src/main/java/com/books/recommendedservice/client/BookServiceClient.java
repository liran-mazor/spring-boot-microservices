package com.books.recommendedservice.client;

import com.books.recommendedservice.dto.BookDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * Client for communicating with BookService via REST API
 */
@Component
public class BookServiceClient {
    
    private final RestTemplate restTemplate;
    private final String bookServiceUrl;
    
    public BookServiceClient(RestTemplate restTemplate, 
                           @Value("${bookservice.url:http://localhost:8080}") String bookServiceUrl) {
        this.restTemplate = restTemplate;
        this.bookServiceUrl = bookServiceUrl;
    }
    
    /**
     * Fetches all books from BookService for recommendations
     */
    public List<BookDto> getAllBooks() {
        try {
            ResponseEntity<List<BookDto>> response = restTemplate.exchange(
                bookServiceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BookDto>>() {}
            );
            
            List<BookDto> books = response.getBody();
            return books != null ? books : Collections.emptyList();
            
        } catch (Exception e) {
            throw new RuntimeException("BookService is currently unavailable. Please try again later.", e);
        }
    }
}