package com.books.recommendedservice.service;

import com.books.recommendedservice.dto.BookDto;
import com.books.recommendedservice.client.BookServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceClientTest {

    @Mock
    private RestTemplate restTemplate;

    private BookServiceClient bookServiceClient;
    private String bookServiceUrl = "http://books-srv:3000/api/books";

    @BeforeEach
    void setUp() {
        bookServiceClient = new BookServiceClient(restTemplate, bookServiceUrl);
    }

    @Test
    @SuppressWarnings("unchecked")
    void getAllBooks_ShouldReturnListOfBooks_WhenServiceRespondsSuccessfully() {
        // Given
        BookDto book1 = new BookDto(1, "Clean Code", 9);
        BookDto book2 = new BookDto(2, "Spring in Action", 8);
        BookDto book3 = new BookDto(3, "Java Concurrency", 7);
        List<BookDto> expectedBooks = Arrays.asList(book1, book2, book3);
        
        ResponseEntity<List<BookDto>> responseEntity = new ResponseEntity<>(expectedBooks, HttpStatus.OK);
        
        when(restTemplate.exchange(
                eq(bookServiceUrl),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // When
        List<BookDto> result = bookServiceClient.getAllBooks();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());
        assertEquals(9, result.get(0).getRating());
    }

    @Test
    @SuppressWarnings("unchecked")
    void getAllBooks_ShouldReturnEmptyList_WhenServiceReturnsEmptyList() {
        // Given
        List<BookDto> emptyBooks = Collections.emptyList();
        ResponseEntity<List<BookDto>> responseEntity = new ResponseEntity<>(emptyBooks, HttpStatus.OK);
        
        when(restTemplate.exchange(
                eq(bookServiceUrl),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // When
        List<BookDto> result = bookServiceClient.getAllBooks();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    void getAllBooks_ShouldReturnEmptyList_WhenServiceReturnsNull() {
        // Given
        ResponseEntity<List<BookDto>> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        
        when(restTemplate.exchange(
                eq(bookServiceUrl),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // When
        List<BookDto> result = bookServiceClient.getAllBooks();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    void getAllBooks_ShouldThrowRuntimeException_WhenRestTemplateThrowsException() {
        // Given
        when(restTemplate.exchange(
                eq(bookServiceUrl),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenThrow(new RestClientException("Connection failed"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookServiceClient.getAllBooks();
        });
        
        assertEquals("BookService is currently unavailable. Please try again later.", exception.getMessage());
        assertTrue(exception.getCause() instanceof RestClientException);
    }
}