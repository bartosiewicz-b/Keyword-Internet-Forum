package com.keyword.keywordspring.exception;

import java.util.NoSuchElementException;

public class UserDoesNotExistException extends NoSuchElementException {

    public UserDoesNotExistException(String username) {
        super("User does not exist for username: " + username);
    }
}
