package com.furongsoft.base.monitor.aop;

import com.furongsoft.base.misc.Tracker;
import com.furongsoft.base.monitor.entities.MethodLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 日志记录器
 *
 * @author Alex
 */
@Aspect
@Component
public class LogAspect {
    private final ThreadLocal<MethodLog> methodLog = new ThreadLocal<>();

    @Pointcut("@within(com.furongsoft.base.monitor.aop.Log)")
    private void log() {
    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        Class<?> clazz = joinPoint.getTarget().getClass();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        methodLog.set(new MethodLog(clazz.getPackage().getName(), clazz.getName(), method.getName(), new Date()));
    }

    @AfterThrowing(value = "log()")
    public void afterThrowing(JoinPoint joinPoint) {
        MethodLog log = methodLog.get();
        log.setEndTime(new Date());
        log.setElapsed(log.getEndTime().getTime() - log.getStartTime().getTime());
        save(log);
    }

    @Around(value = "log()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();

        MethodLog log = methodLog.get();
        log.setEndTime(new Date());
        log.setElapsed(log.getEndTime().getTime() - log.getStartTime().getTime());
        save(log);

        return result;
    }

    private void save(MethodLog methodLog) {
        Tracker.info(methodLog);
    }
}
