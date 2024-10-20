package com.stocks.register.api.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.stocks.register.api.dtos.exceptions.ExceptionDto;
import com.stocks.register.api.exceptions.DuplicatedEntityException;
import com.stocks.register.api.exceptions.GlobalException;
import com.stocks.register.api.exceptions.NotFoundException;
import com.stocks.register.api.exceptions.TryingToManageAdminException;
import com.stocks.register.api.exceptions.UnauthorizedException;
import com.stocks.register.api.exceptions.WrongParametersException;



@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
        NotFoundException.class,
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ExceptionDto handle404Exception(GlobalException e) {
        return ExceptionDto.builder()
            .code(e.getCode())
            .message(e.getMessage())
            .build();
    }

    @ExceptionHandler({
        WrongParametersException.class,
        DuplicatedEntityException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ExceptionDto handle400Exception(GlobalException e) {
        return ExceptionDto.builder()
            .code(e.getCode())
            .message(e.getMessage())
            .build();
    }

    @ExceptionHandler({
        TryingToManageAdminException.class,
        UnauthorizedException.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public @ResponseBody ExceptionDto handle403Exception(GlobalException e) {
        return ExceptionDto.builder()
            .code(e.getCode())
            .message(e.getMessage())
            .build();
    }

}
