package com.books.bookservice.controller;

import com.books.bookservice.model.Book;
import com.books.bookservice.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * All exceptions bubble up to GlobalExceptionHandler
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

     /**
     * Get all books in the system.
     * 
     * Note: In larger datasets, this endpoint should implement
     * pagination using @RequestParam for page/size parameters and return Page<Book>.
     * For this assignment, with a maximum capacity of 20 books, returning all books
     * is acceptable and keeps the implementation simple.
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    /**
     * GET /books/{id} - Returns book by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id) {
        Optional<Book> book = bookService.getBookById(id);
        
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }

    /**
     * POST /books - Add new book
     */
    @PostMapping
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        Book savedBook = bookService.saveBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook); 
    }
}
