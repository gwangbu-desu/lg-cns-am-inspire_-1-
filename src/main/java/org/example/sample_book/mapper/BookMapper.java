package org.example.sample_book.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.sample_book.dto.BookDto;
import org.example.sample_book.dto.BookImageDto;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BookMapper {
    List<BookDto> selectAllBooks(@Param("offset") int offset,@Param("limit") int limit);
    int getTotalRecords();
    Optional<BookDto> selectBookById(int id);
    void insertBook(BookDto bookDto);
    void updateBook(BookDto bookDto);
    void deleteBookById(int id);
    void insertBookImage(BookImageDto bookImageDto);
    void updateBookImage(BookImageDto bookImageDto);
    BookImageDto selectBookImageById(@Param("bookId") int bookId);

    void deleteBookImageById(int bookId);

    BookImageDto selectBookFileInfo(int bookId);
}
