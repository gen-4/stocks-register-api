package com.stocks.register.api.exceptions;

public class NotFoundException extends Exception {

    public NotFoundException(String entity, String criteria) {
        super("Error: " + entity + ": " + criteria + " not found!");
    }
    
}
