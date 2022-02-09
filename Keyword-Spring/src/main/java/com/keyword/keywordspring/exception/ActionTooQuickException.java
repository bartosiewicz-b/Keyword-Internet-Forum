package com.keyword.keywordspring.exception;

public class ActionTooQuickException extends RuntimeException{
    public ActionTooQuickException(String action, int minutesApart){
        super("You can "
                        .concat(action)
                        .concat(" once every ")
                        .concat(Integer.toString(minutesApart))
                        .concat(" minutes."));
    }
}
