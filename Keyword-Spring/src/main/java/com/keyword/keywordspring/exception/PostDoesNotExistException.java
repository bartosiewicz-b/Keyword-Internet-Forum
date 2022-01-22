package com.keyword.keywordspring.exception;

import java.util.NoSuchElementException;

public class PostDoesNotExistException extends NoSuchElementException {

    public PostDoesNotExistException(Long id) {
        super("Post does not exist for id: " + id);
    }
}
