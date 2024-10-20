package com.stocks.register.api.exceptions;

public class ActionFailedException extends GlobalException {

    private static final String code = "exception.id.action.failed";

    public ActionFailedException(String message) {
        super(code, message);
    }
    
}
