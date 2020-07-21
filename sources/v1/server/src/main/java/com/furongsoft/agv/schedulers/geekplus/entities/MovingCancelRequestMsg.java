package com.furongsoft.agv.schedulers.geekplus.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 搬运取消
 *
 * @author Alex
 */
@Getter
@Setter
@AllArgsConstructor
public class MovingCancelRequestMsg {
    /**
     * 消息头参数列表
     */
    private Header header;

    /**
     * 消息体参数列表
     */
    private Body body;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Header {
        /**
         * 防止任务重复提交，唯一码
         */
        private String requestId;

        /**
         * 链路通道唯一编码
         */
        private String channelId;

        /**
         * 客户端码
         */
        private String clientCode;

        /**
         * 对接仓库唯一编码
         */
        private String warehouseCode;

        private String userId;

        private String userKey;

        private String language;

        /**
         * 版本
         */
        private String version;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Body {
        /**
         * 消息类型：此业务功能必须传：MovingRequestMsg
         */
        private String msgType;

        /**
         * 搬运系统任务id
         */
        private String workflowWorkId;
    }
}
