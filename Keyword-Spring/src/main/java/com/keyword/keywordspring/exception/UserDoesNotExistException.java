package com.keyword.keywordspring.exception;

public class UserDoesNotExistException extends RuntimeException{

    public UserDoesNotExistException(String username) {
        super("User does not exist for username: " + username);
    }
}
