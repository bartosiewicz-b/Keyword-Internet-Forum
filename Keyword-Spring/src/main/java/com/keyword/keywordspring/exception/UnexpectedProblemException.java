package com.keyword.keywordspring.exception;

public class UnexpectedProblemException extends RuntimeException{

    public UnexpectedProblemException(String problem){
        super(problem);
    }
}
