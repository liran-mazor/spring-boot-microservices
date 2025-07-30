package com.books.recommendedservice.service;

import com.books.recommendedservice.client.BookServiceClient;
import com.books.recommendedservice.dto.BookDto;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for generating book recommendations
 * Business logic: Get all books and sort by rating (descending)
 */
@Service
public class RecommendedService {
    
    private final BookServiceClient bookServiceClient;
    
    public RecommendedService(BookServiceClient bookServiceClient) {
        this.bookServiceClient = bookServiceClient;
    }
    
    /**
     * Gets recommended books sorted by rating in descending order
     * 
     * Note: We could implement sorting in BookServiceClient, but keeping it here
     * follows Single Responsibility Principle:
     * - Client: Pure data fetching from external service
     * - Service: Business logic and recommendation algorithms
     * This design allows for future extensions like filtering, different sorting
     * strategies, or combining multiple data sources.
     * 
     * @return List of BookDto sorted by rating (highest first)
     */
    public List<BookDto> getRecommendedBooksByRatingDesc() {
        // 1. Fetch all books from BookService
        List<BookDto> allBooks = bookServiceClient.getAllBooks();
        
        // 2. Sort by rating in descending order (highest rating first)
        return allBooks.stream()
                .sorted(Comparator.comparing(BookDto::getRating).reversed())
                .collect(Collectors.toList());
    }
}