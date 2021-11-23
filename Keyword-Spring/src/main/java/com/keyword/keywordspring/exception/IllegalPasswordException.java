package com.keyword.keywordspring.exception;

public class IllegalPasswordException extends RuntimeException{

    public IllegalPasswordException() {
        super("Password does not meet the requirements.");
    }
}
