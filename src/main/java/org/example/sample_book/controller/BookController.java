package org.example.sample_book.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.example.sample_book.dto.BookDto;
import org.example.sample_book.dto.BookImageDto;
import org.example.sample_book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;
    @Value("${book.service.per_page}")
    private int per_page;
    // 페이지네이션 처리
    // 전체 목록 조회 페이지
    @GetMapping("/bookList.do")
    public ModelAndView allList(@RequestParam(value = "page", defaultValue ="1") int page) {
        ModelAndView mv = new ModelAndView("book/bookList");
        List<BookDto> bookDtoList = bookService.getBookList(page);

        int totalRecords = bookService.getTotalRecords();
        int totalPages = (int) Math.ceil((double)totalRecords / per_page);

        // 화면에 표시할 페이지 계산
        int startIndex = Math.max(1, (page - 5));
        int endIndex = Math.min((page + 9), totalPages);

        mv.addObject("bookDtoList", bookDtoList);
        mv.addObject("currentPage", page);
        mv.addObject("totalPages", bookDtoList.size());
        mv.addObject("startPage", startIndex);
        mv.addObject("endPage", endIndex);
        return mv;
    }
    // 검색 정보 페이지
    @GetMapping("/searchBookList.do")
    public ModelAndView search() {
        return new ModelAndView("book/bookList");
    }
    @GetMapping("/bookDetail.do")
    public ModelAndView bookDetail(@RequestParam(value = "bookId") int bookId) {
        ModelAndView mv = new ModelAndView("book/bookDetail");
        BookDto bookDto = bookService.getBookDetail(bookId);
        BookImageDto bookImageDto = bookService.getBookImage(bookDto.getBookId());
        mv.addObject("bookDto", bookDto);
        if (bookImageDto.getType().equals("1")){
            bookImageDto.setImageUrl("file:///"+bookImageDto.getImageUrl());
        }
        mv.addObject("bookImageDto", bookImageDto);
        return mv;
    }
    // 도서 등록 처리
    @PostMapping("/registNewBook.do")
    public String registNewBook(BookDto bookDto,
                                MultipartHttpServletRequest request) {
        bookService.registBook(bookDto,request);
        return "redirect:/book/bookList.do";
    }

    // 도서 등록 페이지/book/registNewBook.do
    @GetMapping("/registNewBook.do")
    public String openRegistBook() {
        return "book/bookRegist";
    }
    // 도서 수정 페이지
    @GetMapping("/updateBook.do")
    public ModelAndView updateBook(@RequestParam(value = "bookId") int bookId) {
        BookDto bookDto = bookService.getBookDetail(bookId);
        ModelAndView mv = new ModelAndView("book/bookUpdate");
        BookImageDto bookImageDto = bookService.getBookImage(bookId);
        mv.addObject("bookDto", bookDto);
        mv.addObject("bookImageDto", bookImageDto);
        return mv;
    }

    // 도서 수정 처리
    @PostMapping("/updateBook.do")
    public String updateBookDetail(BookDto bookDto,MultipartHttpServletRequest request) {
        bookService.updateBook(bookDto,request);
        return "redirect:/book/bookList.do";
    }
//
    // 도서 삭제(페이지 따로 없이 목록으로 리다이렉트
    @PostMapping("/deleteBook.do")
    public String deleteBook(@RequestParam(value = "bookId") int bookId) {
        bookService.deleteBook(bookId);
        return "redirect:/book/bookList.do";
    }
    @GetMapping("/download.do")
    public void downloadImageFile(@RequestParam("bookId") int bookId, HttpServletResponse response) throws Exception{
        BookImageDto bookImageDto = bookService.getBookImageInfo(bookId);
        if (ObjectUtils.isEmpty(bookImageDto)){
            return;
        }
        Path path = Paths.get(bookImageDto.getImageUrl());
        byte[] file = Files.readAllBytes(path);

        response.setContentType("application/octet-stream");
        response.setContentLength(file.length);
        response.setHeader("Content-Disposition", "attachment; fileName=\""+ path.getFileName().toString() +"\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.getOutputStream().write(file);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
}
