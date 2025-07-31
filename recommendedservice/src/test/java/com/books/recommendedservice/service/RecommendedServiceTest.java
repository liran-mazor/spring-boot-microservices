package com.books.recommendedservice.service;

import com.books.recommendedservice.client.BookServiceClient;
import com.books.recommendedservice.dto.BookDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private BookServiceClient bookServiceClient;

    private RecommendedService recommendationService;

    @BeforeEach
    void setUp() {
        recommendationService = new RecommendedService(bookServiceClient);
    }

    @Test
    void getRecommendedBooks_ShouldReturnBooksSortedByRatingDescending() {
        // Given
        BookDto book1 = new BookDto(1, "Average Book", 5);
        BookDto book2 = new BookDto(2, "Excellent Book", 10);
        BookDto book3 = new BookDto(3, "Good Book", 8);
        List<BookDto> unsortedBooks = Arrays.asList(book1, book2, book3);

        when(bookServiceClient.getAllBooks()).thenReturn(unsortedBooks);

        // When
        List<BookDto> result = recommendationService.getRecommendedBooksByRatingDesc();

        // Then
        verify(bookServiceClient, times(1)).getAllBooks();
        assertEquals(3, result.size());
        
        // Verify descending order by rating
        assertEquals(10, result.get(0).getRating());
        assertEquals(8, result.get(1).getRating());
        assertEquals(5, result.get(2).getRating());
    }

    @Test
    void getRecommendedBooks_ShouldReturnEmptyList_WhenNoBooks() {
        // Given
        when(bookServiceClient.getAllBooks()).thenReturn(Collections.emptyList());

        // When
        List<BookDto> result = recommendationService.getRecommendedBooksByRatingDesc();

        // Then
        verify(bookServiceClient, times(1)).getAllBooks();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}