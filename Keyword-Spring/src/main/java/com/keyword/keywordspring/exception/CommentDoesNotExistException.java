package com.keyword.keywordspring.exception;

import java.util.NoSuchElementException;

public class CommentDoesNotExistException extends NoSuchElementException {

    public CommentDoesNotExistException (Long id) {
        super("Comment does not exist for id: " + id);
    }
}
