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
        BookDto book4 = new BookDto(4, "Poor Book", 2);
        List<BookDto> unsortedBooks = Arrays.asList(book1, book2, book3, book4);

        when(bookServiceClient.getAllBooks()).thenReturn(unsortedBooks);

        // When
        List<BookDto> result = recommendationService.getRecommendedBooksByRatingDesc();

        // Then
        verify(bookServiceClient, times(1)).getAllBooks();
        assertEquals(4, result.size());
        
        // Verify descending order by rating
        assertEquals(10, result.get(0).getRating()); // Excellent Book
        assertEquals(8, result.get(1).getRating());  // Good Book
        assertEquals(5, result.get(2).getRating());  // Average Book
        assertEquals(2, result.get(3).getRating());  // Poor Book
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

    @Test
    void getRecommendedBooks_ShouldReturnSingleBook_WhenOnlyOneBook() {
        // Given
        BookDto singleBook = new BookDto(1, "Only Book", 7);
        List<BookDto> books = Collections.singletonList(singleBook);

        when(bookServiceClient.getAllBooks()).thenReturn(books);

        // When
        List<BookDto> result = recommendationService.getRecommendedBooksByRatingDesc();

        // Then
        verify(bookServiceClient, times(1)).getAllBooks();
        assertEquals(1, result.size());
        assertEquals("Only Book", result.get(0).getTitle());
        assertEquals(7, result.get(0).getRating());
    }

    @Test
    void getRecommendedBooks_ShouldHandleIdenticalRatings_MaintainStableSort() {
        // Given
        BookDto book1 = new BookDto(1, "First Book", 8);
        BookDto book2 = new BookDto(2, "Second Book", 8);
        BookDto book3 = new BookDto(3, "Third Book", 8);
        List<BookDto> books = Arrays.asList(book1, book2, book3);

        when(bookServiceClient.getAllBooks()).thenReturn(books);

        // When
        List<BookDto> result = recommendationService.getRecommendedBooksByRatingDesc();

        // Then
        verify(bookServiceClient, times(1)).getAllBooks();
        assertEquals(3, result.size());
        
        // All should have same rating
        result.forEach(book -> assertEquals(8, book.getRating()));
        
        // Stable sort should maintain original order for equal elements
        assertEquals("First Book", result.get(0).getTitle());
        assertEquals("Second Book", result.get(1).getTitle());
        assertEquals("Third Book", result.get(2).getTitle());
    }

    @Test
    void getRecommendedBooks_ShouldPropagateExceptionFromClient() {
        // Given
        RuntimeException clientException = new RuntimeException("BookService is currently unavailable. Please try again later.");
        when(bookServiceClient.getAllBooks()).thenThrow(clientException);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            recommendationService.getRecommendedBooksByRatingDesc();
        });
        
        assertEquals("BookService is currently unavailable. Please try again later.", exception.getMessage());
        verify(bookServiceClient, times(1)).getAllBooks();
    }
}