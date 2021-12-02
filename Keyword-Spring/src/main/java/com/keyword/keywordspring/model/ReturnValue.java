package com.keyword.keywordspring.model;

public class ReturnValue <T> {

    private T value;
    private String error;

    public ReturnValue() {}

    public ReturnValue(T object) { value = object; }

    public boolean isOk() {
        return value != null;
    }

    public boolean isNok() {
        return value == null;
    }

    public T get() {
        return value;
    }

    public void set(T object) {
        value = object;
        error = null;
    }

    public String getError() {
        return error;
    }

    public void setError(String message) {
        value = null;
        error = message;
    }
}
