package com.smart.Exception;

import com.smart.helper.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {
     @ExceptionHandler(FileValidationException .class)
    public String handleBadRequest(FileValidationException ex, HttpSession session, HttpServletRequest request){
         session.setAttribute("message", new Message(ex.getMessage(), "danger"));
         String referer = request.getHeader("Referer");
         return "redirect:" + (referer != null ? referer : "/");
    }
    @ExceptionHandler(CustomException.class)
    public String handleCustomException(CustomException ex, HttpSession session) {
        ex.printStackTrace(); // print the trace on console
        session.setAttribute("message", new Message(ex.getUserMessage(), "danger"));
        return "redirect:" + ex.getRedirectUrl();
    }
    /*this exception automatically called by spring container
    * when container read the properties from application.properties file
    * spring have Configures multipart resolver, which Checks file size before your controller runs*/
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException ex, HttpSession session, HttpServletRequest request) {
        session.setAttribute("message", new Message(ex.getMessage(), "danger"));
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
