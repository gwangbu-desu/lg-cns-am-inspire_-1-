package org.example.sample_book.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(HttpServletRequest request, Exception e) {
        ModelAndView mv = new ModelAndView("/error/default");
        mv.addObject("request", request);
        mv.addObject("message", e.getMessage());
        mv.addObject("stackTrace", e.getStackTrace());
        return mv;
    }
}
