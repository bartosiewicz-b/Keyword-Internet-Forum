package com.keyword.keywordspring.exception;

public class InvalidUsernameOrPasswordException extends RuntimeException{

    public InvalidUsernameOrPasswordException() {
        super("Invalid username or password!");
    }
}
