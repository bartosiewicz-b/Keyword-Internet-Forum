package com.keyword.keywordspring.exception;

public class IllegalEmailException extends RuntimeException{

    public IllegalEmailException(String email) {
        super("Not a valid email address: " + email);
    }
}
