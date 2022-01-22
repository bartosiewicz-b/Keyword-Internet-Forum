package com.keyword.keywordspring.exception;

public class EmailAlreadyTakenException extends IllegalArgumentException {

    public EmailAlreadyTakenException(String email) {
        super("Email " + email + " is already taken.");
    }
}
