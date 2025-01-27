package org.example.sample_book.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class BookImageDto {
    private int imageId;
    private int bookId;
    private String imageUrl;
    private Date createdAt;
    private String type; // 0-URL, 1-업로드파일
}
