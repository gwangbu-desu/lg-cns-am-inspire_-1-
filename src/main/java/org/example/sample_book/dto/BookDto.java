package org.example.sample_book.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class BookDto {
    private int bookId;
    private String title;
    private String author;
    private String publisher;
    private Date publishedDate;
    private String isbn;
    private String description;
    private Date createdAt;
    private Date updatedAt;
}
