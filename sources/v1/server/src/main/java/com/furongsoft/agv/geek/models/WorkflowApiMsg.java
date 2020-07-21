package com.furongsoft.agv.geek.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程任务接口信息
 *
 * @author linyehai
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class WorkflowApiMsg {

    @JsonIgnore
    private Long id;

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
    @JsonIgnore
    private Integer status;
}
