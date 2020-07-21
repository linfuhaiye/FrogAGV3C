package com.furongsoft.agv.schedulers;

/**
 * 调度管理器异常
 *
 * @author Alex
 */
public class SchedulerException {
    /**
     * 无料车异常
     */
    public static class NonMaterialCarException extends Exception {
        private static final long serialVersionUID = 756576488471799397L;
    }

    /**
     * 站点内存在容器异常
     */
    public static class NoEmptySiteException extends Exception {
        private static final long serialVersionUID = -8484327960610273005L;
    }
}