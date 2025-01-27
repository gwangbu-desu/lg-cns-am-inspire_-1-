package org.example.sample_book.service;

import org.example.sample_book.dto.BookDto;
import org.example.sample_book.dto.BookImageDto;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

public interface BookService {
    List<BookDto> getBookList(int page);

    int getTotalRecords();

    BookDto getBookDetail(int bookId);

    BookImageDto getBookImage(int bookId);

    void registBook(BookDto bookDto, MultipartHttpServletRequest request);

    void updateBook(BookDto bookDto, MultipartHttpServletRequest request);

    void deleteBook(int bookId);

    BookImageDto getBookImageInfo(int bookId);
}
