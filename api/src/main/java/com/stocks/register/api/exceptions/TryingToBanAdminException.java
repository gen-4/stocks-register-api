package com.stocks.register.api.exceptions;

public class TryingToBanAdminException extends GlobalException {

    private static final String code = "exception.id.trying_to_ban_admin";

    public TryingToBanAdminException(long id) {
        super(code, "Trying to ban Admin[" + Long.valueOf(id).toString() + "]");
    }
    
}
