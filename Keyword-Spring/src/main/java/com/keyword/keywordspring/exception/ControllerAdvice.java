package com.keyword.keywordspring.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@org.springframework.web.bind.annotation.ControllerAdvice
@Slf4j
public class ControllerAdvice{

    @ExceptionHandler(value = {UnauthorizedException.class})
    public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new HashMap<String, String>().put("error", e.getMessage()));
    }

    @ExceptionHandler(value = {UnexpectedProblemException.class})
    public ResponseEntity<Object> handleUnexpectedProblemException(UnexpectedProblemException e) {
        log.error("Unexpected problem: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new HashMap<String, String>().put("error", "Oops!" + e.getMessage()));
    }
}
