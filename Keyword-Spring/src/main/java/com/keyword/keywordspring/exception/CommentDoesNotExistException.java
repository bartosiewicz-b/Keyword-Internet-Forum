package com.keyword.keywordspring.exception;

public class CommentDoesNotExistException extends RuntimeException{

    public CommentDoesNotExistException (Long id) {
        super("Comment does not exist for id: " + id);
    }
}
