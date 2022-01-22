package com.keyword.keywordspring.exception;

import java.util.NoSuchElementException;

public class GroupDoesNotExistException extends NoSuchElementException {

    public GroupDoesNotExistException(String id) {
        super("Group does not exist for id: " + id);
    }
}
