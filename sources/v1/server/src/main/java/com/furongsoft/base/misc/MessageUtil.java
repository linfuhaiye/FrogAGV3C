package com.furongsoft.base.misc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 国际化消息工具类
 *
 * @author liujianning
 */
@Component
public class MessageUtil {

    private static MessageSource source;

    /**
     * 获取消息信息
     *
     * @param msgKey 消息内容key
     * @param args   其他参数
     * @return 消息信息
     */
    public static String message(String msgKey, Object... args) {
        // 消息的参数化和国际化配置
        Locale locale = LocaleContextHolder.getLocale();
        try {
            msgKey = source.getMessage(msgKey, args, locale);
        } catch (Exception e) {

        }
        return msgKey;
    }

    /**
     * 在Spring里，静态变量/类变量不是对象的属性，而是一个类的属性，不能用@Autowired一个静态变量（对象），使之成为一个SpringBean。
     * 只能通过setter方法注入，并把类注解成为组件
     *
     * @param source MessageSource
     */
    @Autowired
    public void init(MessageSource source) {
        MessageUtil.source = source;
    }
}
