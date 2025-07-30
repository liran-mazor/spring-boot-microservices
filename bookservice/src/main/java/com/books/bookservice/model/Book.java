package com.books.bookservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import jakarta.validation.constraints.*;

@Document(collection = "books")
public class Book {
    
    @Id
    private Integer id; // Auto-generated between 0-20
    
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 50, message = "Title cannot exceed 50 characters")
    private String title;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 10, message = "Rating must be at most 10")
    @Indexed // Index for faster sorting by rating
    private Integer rating;
    
    // Default constructor (required by MongoDB)
    public Book() {}
    
    // Constructor without ID (for new books)
    public Book(String title, Integer rating) {
        this.title = title;
        this.rating = rating;
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", rating=" + rating +
                '}';
    }
}