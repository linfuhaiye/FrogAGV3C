package com.furongsoft.base.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 日志类
 *
 * @author Alex
 */
public class Tracker {
    private static final Logger logger = LoggerFactory.getLogger(Tracker.class);
    private static final Logger networkLogger = LoggerFactory.getLogger("networklog");
    private static final Logger agvLogger = LoggerFactory.getLogger("agvlog");

    /**
     * 输出debug信息
     *
     * @param content 信息
     */
    public static void debug(Object content) {
        logger.debug(makeLog(content));
    }

    /**
     * 输出info信息
     *
     * @param content 信息
     */
    public static void info(Object content) {
        logger.info(makeLog(content));
    }

    /**
     * 输出warn信息
     *
     * @param content 信息
     */
    public static void warn(Object content) {
        logger.warn(makeLog(content));
    }

    /**
     * 输出错误信息
     *
     * @param content 信息
     */
    public static void error(Object content) {
        logger.error(makeLog(content));
    }

    /**
     * 输出错误信息
     *
     * @param e 异常
     */
    public static void error(Throwable e) {
        logger.error(makeLog(getStackTrace(e)));
    }

    /**
     * 输出文件读写相关信息
     *
     * @param content 信息
     */
    public static void file(Object content) {
        logger.info(makeLog(content));
    }

    /**
     * 输出网络信息
     *
     * @param content 信息
     */
    public static void network(Object content) {
        networkLogger.info(makeLog(content));
    }

    /**
     * 输出AGV信息
     *
     * @param content 信息
     */
    public static void agv(Object content) {
        agvLogger.info(makeLog(content));
    }

    /**
     * 获取错误行及相关信息
     *
     * @param content 信息
     * @return 错误信息
     */
    private static String makeLog(Object content) {
        StackTraceElement elem = new Throwable().getStackTrace()[2];

        return elem.getFileName() + "(" + elem.getLineNumber() + "): " + content;
    }

    /**
     * 获取错误信息
     *
     * @param throwable 异常
     * @return 错误信息
     */
    private static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);

        return sw.getBuffer().toString();
    }
}
