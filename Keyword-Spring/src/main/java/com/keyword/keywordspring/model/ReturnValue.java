package com.keyword.keywordspring.model;

import java.util.function.Supplier;

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

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (value != null) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }
}
