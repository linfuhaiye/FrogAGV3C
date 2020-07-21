package com.furongsoft.agv.schedulers.geekplus.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统控制
 *
 * @author Alex
 */
@Getter
@Setter
@AllArgsConstructor
public class WarehouseControlRequestMsg {
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Body {
        /**
         * 消息类型：此业务功能必须传-WarehouseControlRequestMsg
         */
        private String msgType;

        /**
         * 指令类型：ADD_CONTAINER/REMOVE_CONTAINER
         */
        private String commandType;

        /**
         * 容器分类编码
         */
        private String containerCategory;

        /**
         * 容器编码；不填值会根据容器分类自动生成
         */
        private String containerCode;

        /**
         * 目标节点类型（1.停靠点；2.工作站；3.货架点；不传默认按停靠点处理）
         */
        private int destType;

        /**
         * 容器添加的目标点编码
         */
        private String destCode;
    }
}
