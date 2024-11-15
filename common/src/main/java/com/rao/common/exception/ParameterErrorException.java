package com.rao.common.exception;

public class ParameterErrorException extends RuntimeException {
    public ParameterErrorException() {
        super("请求参数错误");
    }
}