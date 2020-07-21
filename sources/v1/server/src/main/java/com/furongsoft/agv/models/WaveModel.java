package com.furongsoft.agv.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 波次信息
 *
 * @author linyehai
 */
@Getter
@Setter
public class WaveModel implements Serializable {
    /**
     * 主键
     */
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
     * 物料ID(产品)
     */
    private long materialId;

    /**
     * 执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date executionTime;

    /**
     * 完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date finishTime;

    /**
     * 状态[0：未配送；1：配送中；2：已完成]
     */
    private int state;

    /**
     * 类型[1：灌装区；2：包装区]
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

    /**
     * 波次信息集合
     */
    private List<WaveModel> waveModels;

    /**
     * 波次详情集合
     */
    private List<WaveDetailModel> waveDetailModels;

    /**
     * 生产线名称
     */
    private String productLineName;

    /**
     * 生产线编号
     */
    private String productLineCode;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料编号
     */
    private String materialCode;

    /**
     * 是否已经叫料
     */
    private Boolean isCalled;

    /**
     * 是否已送达
     */
    private boolean isDelivered;
}
