package com.keyword.keywordspring.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.NoSuchElementException;

@org.springframework.web.bind.annotation.ControllerAdvice
@Slf4j
public class ControllerAdvice{

    @ExceptionHandler(value = {NoSuchElementException.class})
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new HashMap<String, String>().put("error", e.getMessage()));
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new HashMap<String, String>().put("error", e.getMessage()));
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new HashMap<String, String>().put("error", e.getMessage()));
    }
}
