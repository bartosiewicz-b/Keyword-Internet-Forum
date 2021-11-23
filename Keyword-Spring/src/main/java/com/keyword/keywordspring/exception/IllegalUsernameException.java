package com.keyword.keywordspring.exception;

public class IllegalUsernameException extends RuntimeException{

    public IllegalUsernameException(String username) {
        super("Username does not meet requirements: " + username);
    }
}
