package com.furongsoft.agv.schedulers.geekplus.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 执行搬运请求消息
 *
 * @author Alex
 */
@Getter
@Setter
@AllArgsConstructor
public class MovingRequestMsg {
    /**
     * 消息头参数列表
     */
    private Header header;

    /**
     * 消息体参数列表
     */
    private Body body;

    /**
     * 目标点详情
     */
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Dest {
        /**
         * 行号
         */
        private int item;

        /**
         * 目标节点编码
         */
        private String destCode;

        /**
         * 目标节点类型（1.停靠点；2.工作站；3.货架点；4.区域；不传默认按停靠点处理）
         */
        private int destType;

        /**
         * 目标点优先级，默认 1 MES项目必传，MES根据流程优先级排好优先级，1为最优先，2次之，以此类推
         */
        private int destPriority;
    }

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
         * 消息类型：此业务功能必须传：MovingRequestMsg
         */
        private String msgType;

        /**
         * 上游任务单据号
         */
        private String orderNo;

        /**
         * 搬运系统任务id
         */
        private String workflowWorkId;

        /**
         * 起始点位编码
         */
        private String startCode;

        /**
         * 节点类型（1.停靠点；2.工作站；3.货架点；4.区域；）
         */
        private int startType;

        /**
         * 容器二级分类编码
         */
        private String containerCategory;

        /**
         * 容器编码
         */
        private String containerCode;

        /**
         * 流转策略： 1 固定顺序(根据Code升序) 2根据行号排序
         */
        private int flowStrategy;

        /**
         * 流程模式： 1 容器模式(默认) 2 空跑模式
         */
        private int workflowMode;

        /**
         * 优先级，默认 1
         */
        private int priority;

        /**
         * 目标点详情集合
         */
        private Dest[] dests;
    }
}
