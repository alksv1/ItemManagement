package com.rao.common.exception;

// 资源相关错误
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super("资源未找到");
    }
}