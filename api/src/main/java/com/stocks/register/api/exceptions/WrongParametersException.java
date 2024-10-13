package com.stocks.register.api.exceptions;

public class WrongParametersException extends GlobalException {

    private static final String code = "exception.id.wrong_parameters";

    public WrongParametersException(String entity) {
        super(code, "Error: " + entity + " has not received the mandatory parameters!");
    }
    
}
