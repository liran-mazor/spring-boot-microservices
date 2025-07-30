package com.books.bookservice.service;

import com.books.bookservice.model.Book;
import com.books.bookservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    
    private final BookRepository bookRepository;
    
    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public Optional<Book> getBookById(Integer id) {
        return bookRepository.findById(id);
    }
    
    public Book saveBook(Book book) {
        /**
         * Alternative: Redis INCR for atomic ID generation
         * Pros: Better concurrency, no DB queries
         * Cons: Added infrastructure complexity
         * Current approach sufficient for assignment scope
         */
        
        // Get all IDs in sorted order (ascending) with single query
        List<Integer> existingIds = bookRepository.findAllIdsSorted()
            .stream()
            .map(Book::getId)
            .collect(Collectors.toList());
        
        // Fail-fast: Check capacity before expensive gap-finding
        if (existingIds.size() >= 21) {
            throw new RuntimeException("Maximum capacity reached");
        }
        
        // Find first gap in O(n) where n ≤ 21
        int expectedId = 0;
        for (Integer existingId : existingIds) {
            if (existingId != expectedId) {
                break; 
            }
            expectedId++;
        }
        
        book.setId(expectedId);
        return bookRepository.save(book);
    }
}