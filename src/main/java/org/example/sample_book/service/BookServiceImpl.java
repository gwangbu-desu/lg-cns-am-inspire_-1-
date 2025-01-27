package org.example.sample_book.service;

import lombok.extern.slf4j.Slf4j;
import org.example.sample_book.common.FileUtils;
import org.example.sample_book.dto.BookDto;
import org.example.sample_book.dto.BookFileDto;
import org.example.sample_book.dto.BookImageDto;
import org.example.sample_book.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private FileUtils fileUtils;

    @Value("${book.service.per_page}")
    private int PER_PAGE;

    @Override
    public List<BookDto> getBookList(int page) {
        return bookMapper.selectAllBooks((page-1) * PER_PAGE, PER_PAGE);
    }

    @Override
    public int getTotalRecords() {
        return bookMapper.getTotalRecords();
    }

    @Override
    public BookDto getBookDetail(int bookId) {
        Optional<BookDto> bookDtoOptional = bookMapper.selectBookById(bookId);
        return bookDtoOptional.orElse(null);
    }

    @Override
    public BookImageDto getBookImage(int bookId) {
        return bookMapper.selectBookImageById(bookId);
    }

    @Override
    public void registBook(BookDto bookDto, MultipartHttpServletRequest request ) {
        bookMapper.insertBook(bookDto);
        BookImageDto bookImageDto = dup(bookDto, request);
        bookMapper.insertBookImage(bookImageDto);
    }

    @Override
    public void updateBook(BookDto bookDto, MultipartHttpServletRequest request ) {
        bookMapper.updateBook(bookDto);
        BookImageDto bookImageDto = dup(bookDto, request);
        bookMapper.updateBookImage(bookImageDto);
    }

    @Override
    public void deleteBook(int bookId) {
        bookMapper.deleteBookById(bookId);
        bookMapper.deleteBookImageById(bookId);
    }

    @Override
    public BookImageDto getBookImageInfo(int bookId) {
        return bookMapper.selectBookFileInfo(bookId);
    }

    private BookImageDto dup(BookDto bookDto, MultipartHttpServletRequest request) {
        BookImageDto bookImageDto = new BookImageDto();
        String imageUrl = request.getParameter("imageUrl");
        String imageFile = request.getMultipartContentType("imageUpload");
        if (!imageUrl.isEmpty()) {
            bookImageDto.setType("0");
            bookImageDto.setImageUrl(imageUrl);
        }else if(imageFile!=null){
            try{
                BookFileDto bookFileDto = fileUtils.parseFileInfo(bookDto.getBookId(), request);
                bookImageDto.setType("1");
                bookImageDto.setImageUrl(bookFileDto.getStoredFilePath());
            } catch (Exception e){
                log.error(e.getMessage());
            }
        }

        bookImageDto.setBookId(bookDto.getBookId());
        return bookImageDto;
    }
}
