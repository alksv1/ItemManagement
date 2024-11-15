package com.rao.common.exception;

public class RequestMethodException extends RuntimeException{

    public RequestMethodException(){
        super("请求方式错误");
    }
}
