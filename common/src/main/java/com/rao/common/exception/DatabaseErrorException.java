package com.rao.common.exception;

// 系统错误
public class DatabaseErrorException extends RuntimeException {
    public DatabaseErrorException() {
        super("数据库错误");
    }
}