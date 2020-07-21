package com.furongsoft.agv.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.furongsoft.agv.models.WaveModel;
import com.furongsoft.base.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 波次信息
 *
 * @author linyehai
 */
@Entity
@TableName("t_agv_wave")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wave extends BaseEntity {
    @Id
    @GeneratedValue
    private long id;

    /**
     * 编码
     */
    private String code;

    /**
     * 班组唯一标识（uuid）
     */
    private String teamId;

    /**
     * 班组名称
     */
    private String teamName;

    /**
     * 区域ID
     */
    private long areaId;

    /**
     * 产品物料ID
     */
    private long materialId;

    /**
     * 计划执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date executionTime;

    /**
     * 实际完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date finishTime;

    /**
     * 状态[0：未配送；1：配送中；2：已完成]  TODO 后续改用枚举
     */
    private int state;

    /**
     * 类型[1：灌装区；2：包装区]  TODO 后续改用枚举
     */
    private int type;

    /**
     * 是否启用
     */
    private Integer enabled;

    /**
     * 生产订单号
     */
    private String productionOrderNo;

    public Wave(WaveModel waveModel) {
        this.code = waveModel.getCode();
        this.teamId = waveModel.getTeamId();
        this.teamName = waveModel.getTeamName();
        this.areaId = waveModel.getAreaId();
        this.materialId = waveModel.getMaterialId();
        this.executionTime = waveModel.getExecutionTime();
        this.finishTime = waveModel.getFinishTime();
        this.state = waveModel.getState();
        this.type = waveModel.getType();
        this.enabled = waveModel.getEnabled();
        this.productionOrderNo = waveModel.getProductionOrderNo();
    }

    public Wave(String code, String teamId, String teamName, long areaId, long materialId, Date executionTime, int state, int type, Integer enabled, String productionOrderNo) {
        this.code = code;
        this.teamId = teamId;
        this.teamName = teamName;
        this.areaId = areaId;
        this.materialId = materialId;
        this.executionTime = executionTime;
        this.state = state;
        this.type = type;
        this.enabled = enabled;
        this.productionOrderNo = productionOrderNo;
    }
}
