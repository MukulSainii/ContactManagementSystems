package com.smart.Exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {

    private final HttpStatus status;

    public BaseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    public BaseException(String message){
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    public HttpStatus getStatus() {
        return status;
    }
}
