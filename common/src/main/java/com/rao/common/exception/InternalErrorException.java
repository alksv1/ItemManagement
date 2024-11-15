package com.rao.common.exception;

public class InternalErrorException extends RuntimeException {
    public InternalErrorException() {
        super("未知系统错误");
    }
}