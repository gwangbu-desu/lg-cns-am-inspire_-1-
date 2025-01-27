package org.example.sample_book.dto;

import lombok.Data;

@Data
public class BookFileDto {
    private int fileId;
    private int bookId;
    private String originalFileName;
    private String storedFilePath;
    private String fileSize;
}
