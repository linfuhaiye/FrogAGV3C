package com.furongsoft.base.monitor.aop;

import com.furongsoft.base.misc.SecurityUtils;
import com.furongsoft.base.monitor.entities.LogInfo;
import com.furongsoft.base.monitor.mappers.LogInfoDao;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 操作日志记录
 *
 * @author liujianning
 */
@Aspect
@Component
public class WebLogAspect {
    /**
     * 方法开始时间
     */
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();
    /**
     * 操作方法类型
     */
    private final String[] types = new String[]{"add", "edit", "del", "login", "logout", "other"};
    @Autowired
    private LogInfoDao logInfoDao;

    /**
     * 拦截对象
     */
    @Pointcut("execution(public * com.furongsoft.base.*.controllers..*(..))")
    public void logPointCut() {
    }

    /**
     * 调用方法之前
     */
    @Before("logPointCut()")
    public void doBefore() {
        startTime.set(System.currentTimeMillis());
    }

    /**
     * 方法异常截获
     *
     * @param joinPoint 拦截点
     * @param e         异常信息
     */
    @AfterThrowing(pointcut = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        Float execTime = Float.valueOf(((System.currentTimeMillis() - startTime.get()) / 1000));
        log(joinPoint, e, execTime);
    }

    /**
     * 执行方法后
     *
     * @param pjp 拦截点
     * @return 执行对象返回值
     * @throws Throwable 异常信息
     */
    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        // 执行方法返回值
        Object result = pjp.proceed();
        Float execTime = Float.valueOf(((System.currentTimeMillis() - startTime.get()) / 1000));
        log(pjp, null, execTime);
        return result;
    }

    /**
     * 日志入库
     *
     * @param joinPoint 拦截点
     * @param e         异常信息
     * @param execTime  执行时间
     */
    private void log(JoinPoint joinPoint, Exception e, Float execTime) {
        if (null == SecurityUtils.getCurrentUser()) {
            return;
        }

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取控制器类名称
        String className = joinPoint.getTarget().getClass().getName();
        // 获取被拦截的方法
        Method method = signature.getMethod();
        // 获取被拦截的方法名
        String methodName = method.getName();
        // 模块名
        String modelName = className.substring(className.lastIndexOf('.') + 1);
        // 默认操作方法
        String type = "other";
        // 错误消息
        String errMsg = "";
        // 操作用户
        String username = "未登录";
        // 用户ID
        Long userId = null;
        // 公司ID
        String company = "未登录";

        for (String s : types) {
            if (methodName.toLowerCase().contains(s)) {
                type = s;
                break;
            }
        }

        // 添加异常消息
        if (null != e) {
            // 若没有异常消息，保存异常类型
            if (null == e.getMessage()) {
                errMsg = e.toString();
            } else {
                errMsg = e.getMessage();
            }
        }

        if (null != SecurityUtils.getCurrentUser()) {
            username = SecurityUtils.getCurrentUser().getUsername();
            userId = SecurityUtils.getCurrentUser().getId();
            // todo
            company = "test";
        }

        LogInfo logInfo = new LogInfo();
        logInfo.setOperation(methodName);
        logInfo.setExecTime(execTime);
        logInfo.setIp(getIpAddr(request));
        logInfo.setModel(modelName);
        logInfo.setType(type);
        logInfo.setCreateUser(userId);
        logInfo.setCompanyId(company);
        logInfo.setErrLog(errMsg);
        logInfo.setUsername(username);
        logInfoDao.insert(logInfo);
        startTime.remove();
    }

    /**
     * 获取ip
     *
     * @param request 请求对象
     * @return ip地址
     */
    private String getIpAddr(HttpServletRequest request) {
        String ipAddress;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if ("127.0.0.1".equals(ipAddress)) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    if (null != inet) {
                        ipAddress = inet.getHostAddress();
                    }
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            // "***.***.***.***".length()
            if (ipAddress != null && ipAddress.length() > 15) {
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }

        return ipAddress;
    }
}
