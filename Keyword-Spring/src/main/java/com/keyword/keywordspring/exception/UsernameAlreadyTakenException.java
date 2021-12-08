package com.keyword.keywordspring.exception;

public class UsernameAlreadyTakenException extends RuntimeException{

    public UsernameAlreadyTakenException(String username) {
        super("User with username: " + username + " is already registered.");
    }
}
