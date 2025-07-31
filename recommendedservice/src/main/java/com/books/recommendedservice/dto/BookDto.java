package com.books.recommendedservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Data Transfer Object for Book data received from BookService
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDto {
    private Integer id;
    private String title;
    private Integer rating;

    public BookDto() {}

    public BookDto(Integer id, String title, Integer rating) {
        this.id = id;
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
        return "BookDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", rating=" + rating +
                '}';
    }
}