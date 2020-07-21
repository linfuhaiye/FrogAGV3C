package com.furongsoft.agv.schedulers.geekplus.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 搬运回调
 *
 * @author Alex
 */
@Getter
@Setter
@AllArgsConstructor
public class MovingCallbackMsg {
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
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
         * 流程任务Id
         */
        private Integer workflowWorkId;

        /**
         * 上游标识号
         */
        private String orderNo;

        /**
         * 搬运任务状态
         */
        private Integer workflowPhase;

        /**
         * 机器人任务阶段
         */
        private String taskPhase;

        /**
         * 起始点
         */
        private String startCode;

        /**
         * 起始点类型(1.停靠点；2.工作站；3.货架点；4.区域)
         */
        private Integer startType;

        /**
         * 流程模式(1.容器模式；2.空跑模式)
         */
        private Integer workflowMode;

        /**
         * 目标点
         */
        private String destCode;

        /**
         * 目标点类型(1.停靠点；2.工作站；3.货架点；4.区域)
         */
        private Integer destType;

        /**
         * 容器号
         */
        private String containerCode;

        /**
         * 容器分类编码
         */
        private String containerCategory;

        /**
         * 机器人编号
         */
        private Integer robotId;

        /**
         * 数据状态(1.正常；0.已删除)
         */
        private Integer status;
    }
}
