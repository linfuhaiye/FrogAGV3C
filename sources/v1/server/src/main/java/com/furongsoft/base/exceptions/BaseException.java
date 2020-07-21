package com.furongsoft.base.exceptions;

/**
 * 基础异常
 *
 * @author Alex
 */
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = -1479057379313036667L;

    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }

    /**
     * 文件上传失败异常
     */
    public static class UploadFileFailException extends BaseException {
        private static final long serialVersionUID = -5136939809126048194L;
    }

    /**
     * 错误的提交参数异常
     */
    public static class IllegalArgumentException extends BaseException {
        private static final long serialVersionUID = 7947900542957674089L;
    }
}
