package com.furongsoft.agv.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.furongsoft.base.entities.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * AGV的配送任务信息
 *
 * @author linyehai
 */
@Entity
@TableName("t_agv_delivery_task")
@Getter
@Setter
@NoArgsConstructor
public class DeliveryTask extends BaseEntity {
    @Id
    @GeneratedValue
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
     * 状态[0：待接单；1：取货中；2：配送中；3：已完成；4：已取消]  TODO 后续改用枚举
     */
    private int state;

    /**
     * 是否启用
     */
    private Integer enabled;

    /**
     * 配送波次
     */
    private String waveCode;

    public DeliveryTask(String taskNo, String workflowWorkId, long startSiteId, long endSiteId, long materialBoxId, String agvUuid, int state, String waveCode) {
        this.taskNo = taskNo;
        this.workflowWorkId = workflowWorkId;
        this.startSiteId = startSiteId;
        this.endSiteId = endSiteId;
        this.materialBoxId = materialBoxId;
        this.agvUuid = agvUuid;
        this.state = state;
        this.waveCode = waveCode;
    }
}
