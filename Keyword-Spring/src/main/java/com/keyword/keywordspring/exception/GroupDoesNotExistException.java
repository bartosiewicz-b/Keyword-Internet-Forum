package com.keyword.keywordspring.exception;

public class GroupDoesNotExistException extends RuntimeException{

    public GroupDoesNotExistException(Long id) {
        super("Group does not exist for id: " + id);
    }
}
