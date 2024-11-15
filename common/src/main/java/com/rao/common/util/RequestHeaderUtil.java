package com.rao.common.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public class RequestHeaderUtil {

    /**
     * 获取当前请求的 HttpServletRequest 对象
     *
     * @return HttpServletRequest
     */
    private static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest();
        }
        throw new IllegalStateException("当前请求不存在");
    }

    /**
     * 根据请求头名称获取当前请求的请求头值
     *
     * @param headerName 请求头名称
     * @return 请求头值
     */
    public static String getHeader(String headerName) {
        HttpServletRequest request = getCurrentRequest();
        return request.getHeader(headerName);
    }

    /**
     * 获取当前请求中的 token
     *
     * @return token 值
     */
    public static String getToken() {
        return getHeader("token");
    }

}
