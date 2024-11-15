package com.rao.common.exception.handler;

import com.rao.common.exception.AccountOrPasswordErrorException;
import com.rao.common.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<?> handleUnknownException(Exception ex) {
        ex.printStackTrace();
        return Result.InternalSystemError();
    }

    @ExceptionHandler(AccountOrPasswordErrorException.class)
    public Result<?> handleAccountOrPasswordErrorException(Exception ex) {
        ex.printStackTrace();
        return Result.accountOrPasswordError();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return Result.parameterError();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return Result.requestMethodError();
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Result<?> handleNoResourceFoundException(NoResourceFoundException ex){
        return Result.badGateway();
    }
}
