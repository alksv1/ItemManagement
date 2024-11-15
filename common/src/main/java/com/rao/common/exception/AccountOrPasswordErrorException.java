package com.rao.common.exception;

public class AccountOrPasswordErrorException extends RuntimeException {
    public AccountOrPasswordErrorException() {
        super("账号/密码错误");
    }
}