package com.stocks.register.api.exceptions;

public class TryingToManageAdminException extends GlobalException {

    private static final String code = "exception.id.trying_to_ban_admin";

    public TryingToManageAdminException(long id) {
        super(code, "Trying to ban Admin[" + Long.valueOf(id).toString() + "]");
    }
    
}
