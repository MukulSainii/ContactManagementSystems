package com.smart.Exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {

    private final HttpStatus status;
    private final String userMessage;

    public BaseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.userMessage = message;
    }
    public BaseException(String message){
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.userMessage = message;
    }
    public BaseException(String message, String userMessage){
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.userMessage = userMessage;
    }
    public BaseException(String message, String userMessage, HttpStatus status) {
        super(message);
        this.status = status;
        this.userMessage = userMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
