package com.furongsoft.agv.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 波次详情信息
 *
 * @author linyehai
 */
@Getter
@Setter
public class WaveDetailModel implements Serializable {
    /**
     * 主键
     */
    private long id;

    /**
     * 编码
     */
    private String code;

    /**
     * 波次编号
     */
    private String waveCode;

    /**
     * 物料ID(原料)
     */
    private long materialId;

    /**
     * 数量
     */
    private int count;

    /**
     * 状态[0：未配送；1：配送中；2：已完成]
     */
    private int state;

    /**
     * 是否启用
     */
    private Integer enabled;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料编号
     */
    private String materialCode;

    /**
     * 叫料ID
     */
    private Long callId;

    /**
     * 叫料状态
     */
    private Integer callState;

    /**
     * (1：灌装区；2：包装区；3：消毒间；4：拆包间)
     */
    private Integer areaType;
}
