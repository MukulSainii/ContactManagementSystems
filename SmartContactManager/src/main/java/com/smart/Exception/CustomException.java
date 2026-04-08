package com.smart.Exception;

public class CustomException extends RuntimeException{
    private final String redirectUrl;
    private final String userMessage;

    public CustomException(String message,String userMessage, String redirectUrl) {
        super(message);
        this.redirectUrl = redirectUrl;
        this.userMessage = userMessage;
    }
    public CustomException(String message, String redirectUrl) {
        super(message);
        this.redirectUrl = redirectUrl;
        this.userMessage = message;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
    public String getUserMessage(){
        return userMessage;
    }
}
