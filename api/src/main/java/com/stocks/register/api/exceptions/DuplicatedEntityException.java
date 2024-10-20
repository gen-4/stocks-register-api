package com.stocks.register.api.exceptions;

public class DuplicatedEntityException extends GlobalException {

    private static final String code = "exception.id.action.duplicate_entity";

    public DuplicatedEntityException(String entity) {
        super(code, entity + " was already created");
    }
    
}