package com.stocks.register.api.exceptions;

public class UnauthorizedException extends GlobalException {

    private static final String code = "exception.id.unauthorized";

    public UnauthorizedException(String email) {
        super(code, "Error: Authorization for user with email [" + email + "] failed");
    }
    
}
