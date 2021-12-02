package com.keyword.keywordspring.exception;

public class PostDoesNotExistException extends RuntimeException{

    public PostDoesNotExistException(Long id) {
        super("Post does not exist for id: " + id);
    }
}
