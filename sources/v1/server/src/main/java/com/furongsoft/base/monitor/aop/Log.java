package com.furongsoft.base.monitor.aop;

import java.lang.annotation.*;

/**
 * 日志注解
 *
 * @author Alex
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
}
