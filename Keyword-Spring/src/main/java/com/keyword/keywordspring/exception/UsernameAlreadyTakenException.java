package com.keyword.keywordspring.exception;

public class UsernameAlreadyTakenException extends IllegalArgumentException {

    public UsernameAlreadyTakenException(String username) {
        super("Username " + username + " is already taken.");
    }
}
