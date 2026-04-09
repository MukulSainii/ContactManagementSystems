package com.smart.Exception;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends BaseException {
    public OrderNotFoundException(String message){
        super(message, HttpStatus.NOT_FOUND);
    }


}
