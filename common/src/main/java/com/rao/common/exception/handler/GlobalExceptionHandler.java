package com.rao.common.exception.handler;

import com.rao.common.exception.*;
import com.rao.common.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //这里是捕获自定义异常
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

    @ExceptionHandler(InternalErrorException.class)
    public Result<?> handleInternalErrorException(Exception ex) {
        ex.printStackTrace();
        return Result.InternalSystemError();
    }

    @ExceptionHandler(NoPermissionException.class)
    public Result<?> handleNoPermissionException(Exception ex) {
        ex.printStackTrace();
        return Result.noPermission();
    }

    @ExceptionHandler(ParameterErrorException.class)
    public Result<?> handleParameterErrorException(Exception ex) {
        ex.printStackTrace();
        return Result.parameterError();
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public Result<?> handleResourceAlreadyExistsException(Exception ex) {
        ex.printStackTrace();
        return Result.resourceAlreadyExists();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public Result<?> handleResourceNotFoundException(Exception ex) {
        ex.printStackTrace();
        return Result.resourceNotFound();
    }

    @ExceptionHandler(TokenInvalidException.class)
    public Result<?> handleTokenInvalidException(Exception ex) {
        ex.printStackTrace();
        return Result.tokenInvalid();
    }

    //下面是捕获其他异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return Result.parameterError();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return Result.requestMethodError();
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Result<?> handleNoResourceFoundException(NoResourceFoundException ex) {
        return Result.badGateway();
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public Result<?> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        return Result.noPermission();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<?> handlerMissingServletRequestParameterException(Exception ex) {
        return Result.parameterError();
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<?> handlerSQLIntegrityConstraintViolationException(Exception ex) {
        return Result.resourceNotFound();
    }
}
