package com.furongsoft.agv.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 配送任务信息
 *
 * @author linyehai
 */
@Getter
@Setter
@ToString
public class DeliveryTaskModel implements Serializable {
    private long id;

    /**
     * 配送任务单号(全系统唯一,自动生成)
     */
    private String taskNo;

    /**
     * WCS任务索引
     */
    private String workflowWorkId;

    /**
     * 起始站点ID
     */
    private long startSiteId;

    /**
     * 终点站点ID
     */
    private long endSiteId;

    /**
     * 料框ID
     */
    private long materialBoxId;

    /**
     * AGV小车唯一标识
     */
    private String agvUuid;

    /**
     * 状态[0：待接单；1：取货中；2：配送中；3：已完成；4：已取消]
     */
    private int state;

    /**
     * 类型[1：消毒-灌装；2：灌装-消毒；3：包材-拆包；4：拆包-包材；5：包材-包装；6：包装-包材]
     */
    private int type;

    /**
     * 是否启用
     */
    private Integer enabled;

    /**
     * 生产线
     */
    private String productLine;

    /**
     * 波次编号
     */
    private String waveCode;

    /**
     * 区域的编码 （3B、3C）
     */
    private String areaCoding;
}
