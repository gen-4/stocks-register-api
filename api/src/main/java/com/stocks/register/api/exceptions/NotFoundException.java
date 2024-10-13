package com.stocks.register.api.exceptions;

public class NotFoundException extends GlobalException {

    private static final String code = "exception.id.not_found";

    public NotFoundException(String entity, String criteria) {
        super(code, "Error: " + entity + " -> " + criteria + " not found!");
    }
    
}
