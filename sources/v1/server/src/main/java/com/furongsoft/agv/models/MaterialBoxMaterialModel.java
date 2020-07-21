package com.furongsoft.agv.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 料框-物料信息
 *
 * @author linyehai
 */
@Getter
@Setter
public class MaterialBoxMaterialModel implements Serializable {
    private long id;

    /**
     * 料框ID
     */
    private long materialBoxId;

    /**
     * 物料ID(原料)
     */
    private long materialId;

    /**
     * 数量
     */
    private int count;

    /**
     * 状态[0：未验收；1：已验收]
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
     * 物料唯一标识
     */
    private String materialUUID;

    /**
     * 物料规格
     */
    private String materialSpecs;

    /**
     * 物料单位
     */
    private String materialUnit;

}
