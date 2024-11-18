package com.rao.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer status;
    private String message;
    private T data;

    public static <T> Result<T> OK(T data) {
        return new Result<>(0, "请求成功！", data);
    }

    // 通用错误
    public static <T> Result<T> parameterError() {
        return new Result<>(1001, "请求参数错误", null);
    }

    public static <T> Result<T> requestMethodError() {
        return new Result<>(1002, "请求方式错误", null);
    }

    // 身份认证和授权错误
    public static <T> Result<T> tokenInvalid() {
        return new Result<>(2001, "Token无效/缺失", null);
    }

    public static <T> Result<T> noPermission() {
        return new Result<>(2002, "无权限访问资源/未登录", null);
    }

    public static <T> Result<T> accountOrPasswordError() {
        return new Result<>(2003, "账号/密码错误", null);
    }

    public static <T> Result<T> captchaExpireError() {
        return new Result<>(2004, "验证码过期错误", null);
    }

    // 资源相关错误
    public static <T> Result<T> resourceNotFound() {
        return new Result<>(3001, "资源未找到", null);
    }

    public static <T> Result<T> resourceAlreadyExists() {
        return new Result<>(3002, "资源已存在", null);
    }

    // 系统错误
    public static <T> Result<T> databaseError() {
        return new Result<>(5001, "数据库错误", null);
    }

    public static <T> Result<T> InternalSystemError() {
        return new Result<>(5002, "服务器内部错误", null);
    }

    public static <T> Result<T> badGateway() {
        return new Result<>(5003, "BadGateway", null);
    }
}
