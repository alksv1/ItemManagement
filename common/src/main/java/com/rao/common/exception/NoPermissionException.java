package com.rao.common.exception;

public class NoPermissionException extends RuntimeException {
    public NoPermissionException() {
        super("无权限访问资源");
    }
}