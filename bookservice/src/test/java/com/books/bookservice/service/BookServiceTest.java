package com.books.bookservice.service;

import com.books.bookservice.model.Book;
import com.books.bookservice.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldSaveBookSuccessfully() {
        // Given
        Book inputBook = new Book("Test Book", 8);
        Book savedBook = new Book("Test Book", 8);
        savedBook.setId(1);
        
        when(bookRepository.findAllIdsSorted()).thenReturn(Arrays.asList(
            createBookWithId(2), createBookWithId(3), createBookWithId(5)
        )); // Gap at ID 1
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);
        
        // When
        Book result = bookService.saveBook(inputBook);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test Book", result.getTitle());
        assertEquals(8, result.getRating());
        verify(bookRepository).save(inputBook);
    }

    @Test
    void shouldThrowExceptionWhenCapacityReached() {
        // Given
        Book book = new Book("Test Book", 8);
        // Mock 21 books (0-20) to trigger capacity error
        when(bookRepository.findAllIdsSorted()).thenReturn(Arrays.asList(
            createBookWithId(0), createBookWithId(1), createBookWithId(2),
            createBookWithId(3), createBookWithId(4), createBookWithId(5),
            createBookWithId(6), createBookWithId(7), createBookWithId(8),
            createBookWithId(9), createBookWithId(10), createBookWithId(11),
            createBookWithId(12), createBookWithId(13), createBookWithId(14),
            createBookWithId(15), createBookWithId(16), createBookWithId(17),
            createBookWithId(18), createBookWithId(19), createBookWithId(20)
        ));
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> bookService.saveBook(book));
        
        assertEquals("Maximum capacity reached", exception.getMessage());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void shouldReturnAllBooks() {
        // Given
        List<Book> books = Arrays.asList(
            new Book("Book 1", 7),
            new Book("Book 2", 9)
        );
        when(bookRepository.findAll()).thenReturn(books);
        
        // When
        List<Book> result = bookService.getAllBooks();
        
        // Then
        assertEquals(2, result.size());
        assertEquals("Book 1", result.get(0).getTitle());
        assertEquals("Book 2", result.get(1).getTitle());
    }

    @Test
    void shouldReturnBookById() {
        // Given
        Book book = new Book("Test Book", 8);
        book.setId(1);
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        
        // When
        Optional<Book> result = bookService.getBookById(1);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals("Test Book", result.get().getTitle());
        assertEquals(8, result.get().getRating());
    }

    @Test
    void shouldReturnEmptyWhenBookNotFound() {
        // Given
        when(bookRepository.findById(999)).thenReturn(Optional.empty());
        
        // When
        Optional<Book> result = bookService.getBookById(999);
        
        // Then
        assertFalse(result.isPresent());
    }

    private Book createBookWithId(Integer id) {
        Book book = new Book("Book " + id, 5);
        book.setId(id);
        return book;
    }
}