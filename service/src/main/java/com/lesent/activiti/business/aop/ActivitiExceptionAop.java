package com.lesent.activiti.business.aop;

import com.lesent.activiti.common.dto.ResEntityUtils;
import com.lesent.activiti.common.enums.RestStsEnum;
import com.lsdk.activiti.business.exception.ActivitiBusiException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ActivitiExceptionAop {

    private static final Logger logger = LoggerFactory.getLogger(ActivitiBusiException.class);

    @Pointcut(value = "execution(* com.lsdk.activiti.business.service.impl.*.*(..))")
    public void pointCut(){}


    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint point) {
        try {
            Object o =  point.proceed();
            return o;
        } catch (Throwable e) {
            return processException(e);
        }
    }

    private Object processException(Throwable e){
        if(e instanceof ActivitiBusiException){
            //处理业务异常
            ActivitiBusiException var = (ActivitiBusiException)e;
            logger.info("  >>> appear exception code :{} msg :{} <<<  ",var.getCode(),var.getMsg());
            return ResEntityUtils.error(var.getCode(),var.getMsg());
        }else {
            //非业务异常
            logger.info(" >>> process error error :{} cause:{}<<< ",e.getMessage(),e.getCause());
            logger.error(">>> error ",e);
            return ResEntityUtils.error(RestStsEnum.FAILURE.code,e.toString());
        }
    }
}
