package com.rao.common.exception;

public class TokenInvalidException extends RuntimeException {
    public TokenInvalidException() {
        super("Token无效/缺失");
    }
}