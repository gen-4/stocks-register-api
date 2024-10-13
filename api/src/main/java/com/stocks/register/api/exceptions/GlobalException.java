package com.stocks.register.api.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;




@Data
@EqualsAndHashCode(callSuper=true)
public abstract class GlobalException extends Exception {

    private String code;
    private String message;

    public GlobalException(String code, String message) {
        super(message);

        this.code = code;
        this.message = message;
    }
    
}
