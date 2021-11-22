package com.keyword.keywordspring.exception;

public class EmailAlreadyTakenException extends RuntimeException{

    public EmailAlreadyTakenException(String email) {
        super("User with email already registered: " + email);
    }
}
