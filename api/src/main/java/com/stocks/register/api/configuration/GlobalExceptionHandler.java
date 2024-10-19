package com.stocks.register.api.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.stocks.register.api.dtos.exceptions.ExceptionDto;
import com.stocks.register.api.exceptions.GlobalException;
import com.stocks.register.api.exceptions.NotFoundException;
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
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ExceptionDto handle400Exception(GlobalException e) {
        return ExceptionDto.builder()
            .code(e.getCode())
            .message(e.getMessage())
            .build();
    }

}
