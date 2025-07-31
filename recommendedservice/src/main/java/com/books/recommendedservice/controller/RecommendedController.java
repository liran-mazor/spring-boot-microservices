package com.books.recommendedservice.controller;

import com.books.recommendedservice.dto.BookDto;
import com.books.recommendedservice.service.RecommendedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * All exceptions bubble up to GlobalExceptionHandler
 */
@RestController
@RequestMapping("/api/recommended")
public class RecommendedController {
    
    private final RecommendedService recommendationService;
    
    public RecommendedController(RecommendedService recommendationService) {
        this.recommendationService = recommendationService;
    }
    
    /**
     * GET /api/recommended - Returns books sorted by rating (descending)
     * 
     * This endpoint calls BookService, retrieves all books, sorts them by rating
     * in descending order, and returns the recommended list.
     */
    @GetMapping
    public ResponseEntity<List<BookDto>> getRecommendedBooks() {
        List<BookDto> recommendedBooks = recommendationService.getRecommendedBooksByRatingDesc();
        return ResponseEntity.ok(recommendedBooks);
    }
}