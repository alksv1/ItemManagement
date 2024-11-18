package com.rao.userservice.aspect;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rao.common.exception.ParameterErrorException;
import com.rao.common.util.JwtUtil;
import com.rao.common.util.RequestHeaderUtil;
import com.rao.userservice.mapper.LogMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    @Autowired
    private LogMapper logMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Pointcut("@annotation(com.rao.userservice.anno.LogAnno)")
    public void logPointCut() {
    }

    //借还微服务需要记录数量变化

    @After("logPointCut()")
    public void LogOperation(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length == 0 || !(args[0] instanceof String id)) throw new ParameterErrorException();

        String methodName = joinPoint.getSignature().getName();
        String userId = jwtUtil.getUserIdFromToken(RequestHeaderUtil.getToken());
        String json = null;
        if (args.length == 2) {
            json = JSON.toJSONString(args[1]);
        }
        logMapper.insertUserLog(userId, id, methodName, json);
    }
}
