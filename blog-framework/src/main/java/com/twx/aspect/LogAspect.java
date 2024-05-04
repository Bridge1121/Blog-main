package com.twx.aspect;

import com.alibaba.fastjson.JSON;
import com.twx.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Slf4j
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("@annotation(com.twx.annotation.SystemLog)")
    public void pt(){

    }
    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object ret;
        try {
            handleBefore(joinPoint);
            ret = joinPoint.proceed();
            handleAfter(ret);
        } finally {
            // 结束后换行 
            logger.info("=======End=======" + System.lineSeparator());
        }
        return null;
    }

    private void handleAfter(Object ret) {
        // 打印出参
        logger.info("Response : {}", JSON.toJSONString(ret));
    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {

        ServletRequestAttributes requestContextHolder = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestContextHolder.getRequest();
        //获取被增强方法上的注解对象
        SystemLog systemLog = getSystemLog(joinPoint);
        logger.info("=======Start=======");
        // 打印请求 URL
        logger.info("URL : {}",request.getRequestURI());
        // 打印描述信息
        logger.info("BusinessName : {}",systemLog.businessName() );
        // 打印 Http method
        logger.info("HTTP Method : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        logger.info("Class Method : {}.{}",joinPoint.getSignature().getDeclaringTypeName(),((MethodSignature) joinPoint.getSignature()).getName() );
        // 打印请求的 IP
        logger.info("IP : {}",request.getRemoteHost());
        // 打印请求入参
        logger.info("Request Args : {}", JSON.toJSONString(joinPoint.getArgs()));

    }

    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        SystemLog systemLog = methodSignature.getMethod().getAnnotation(SystemLog.class);
        return systemLog;
    }

}
