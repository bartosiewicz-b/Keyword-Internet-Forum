package com.keyword.keywordspring.exception;

public class InvalidTokenException extends RuntimeException{

    public InvalidTokenException() {
        super("Token is invalid.");
    }
}
