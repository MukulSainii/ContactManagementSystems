package com.smart.Exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    public NotFoundException(String message){
        super(message, HttpStatus.NOT_FOUND);
    }
    public NotFoundException(String message, String userMessage){
            super(message, userMessage, HttpStatus.NOT_FOUND);
        }

}
