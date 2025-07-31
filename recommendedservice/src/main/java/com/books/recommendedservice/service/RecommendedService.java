package com.books.recommendedservice.service;

import com.books.recommendedservice.client.BookServiceClient;
import com.books.recommendedservice.dto.BookDto;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendedService {
    
    private final BookServiceClient bookServiceClient;
    
    public RecommendedService(BookServiceClient bookServiceClient) {
        this.bookServiceClient = bookServiceClient;
    }
    
    /**
     * Gets recommended books sorted by rating in descending order
     * @return List of BookDto sorted by rating (highest first)
     * 
     * DESIGN PATTERN: Strategy Pattern could be implemented here
     * for different sorting strategies (by rating, by title, by date)
     * Current implementation: Single strategy (rating descending)
     * Future: Pluggable SortingStrategy interface
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