package com.smart.Exception;

import com.smart.helper.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
     @ExceptionHandler(FileValidationException .class)
    public String handleBadRequest(BadRequestException ex, HttpSession session, HttpServletRequest request){
         session.setAttribute("message", new Message(ex.getMessage(), "danger"));
         String referer = request.getHeader("Referer");
         return "redirect:" + (referer != null ? referer : "/");
    }

}
