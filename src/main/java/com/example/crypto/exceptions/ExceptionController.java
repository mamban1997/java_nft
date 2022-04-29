package com.example.crypto.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
class ExceptionController extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ NftNotFoundException.class })
    public String handleException() {
        return "redirect:/";
    }
}
