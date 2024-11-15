package com.rao.common.exception;

public class ExternalServiceFailureException extends RuntimeException {
    public ExternalServiceFailureException() {
        super("外部服务调用失败");
    }
}