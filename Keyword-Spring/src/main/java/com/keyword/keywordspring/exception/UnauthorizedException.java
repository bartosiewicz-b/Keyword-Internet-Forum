package com.keyword.keywordspring.exception;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException() {
        super("Unauthorized!");
    }
}
