package com.keyword.keywordspring.model;

public enum VoteType {
    UP (1), DOWN (-1);

    private final Integer value;

    VoteType (Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
