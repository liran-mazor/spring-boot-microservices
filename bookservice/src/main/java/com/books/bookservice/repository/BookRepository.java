package com.books.bookservice.repository;

import com.books.bookservice.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends MongoRepository<Book, Integer> {
    
    /**
     * Optimized query for ID gap-finding:
     * - fields = "{ 'id' : 1 }" : Only fetch ID field (reduces bandwidth)
     * - sort = "{ 'id' : 1 }" : Leverage MongoDB's indexed sorting
     * Single query instead of 21 separate existsById() calls
     */
    @Query(value = "{}", fields = "{ 'id' : 1 }", sort = "{ 'id' : 1 }")
    List<Book> findAllIdsSorted();
}