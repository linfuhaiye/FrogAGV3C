package com.furongsoft.agv.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 叫料信息前端对象
 *
 * @author linyehai
 */
@Getter
@Setter
public class CallMaterialModel implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 物料ID(原料)
     */
    private long materialId;

    /**
     * 叫料数量
     */
    private int count;

    /**
     * 验收数量
     */
    private Integer acceptanceCount;

    /**
     * 状态[1：未配送；2：配送中；3：已完成；4：已取消]
     */
    private int state;

    /**
     * 叫料时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date callTime;

    /**
     * 配送波次详情编号
     */
    private String waveDetailCode;

    /**
     * 类型[1：灌装区；2：包装区；3：消毒间；4：拆包间]
     */
    private int type;

    /**
     * 取消叫料原因
     */
    private String cancelReason;

    /**
     * 是否启用
     */
    private Integer enabled;

    /**
     * 原料名称
     */
    private String materialName;

    /**
     * 原料编号
     */
    private String materialCode;

    /**
     * 原料唯一标识
     */
    private String materialUuid;

    /**
     * 原料规格
     */
    private String materialSpecs;

    /**
     * 原料单位
     */
    private String materialUnit;

    /**
     * 原料批次
     */
    private String materialBatch;

    /**
     * 班组唯一标识（青蛙uuid）
     */
    private String teamId;

    /**
     * 区域ID（产线ID）
     */
    private Integer areaId;

    /**
     * 站点ID
     */
    private Long siteId;

    /**
     * 波次编码
     */
    private String waveCode;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品UUID
     */
    private String productUuid;

    /**
     * 生产线编号
     */
    private String productLineCode;

    /**
     * 班组名称
     */
    private String teamName;
}
