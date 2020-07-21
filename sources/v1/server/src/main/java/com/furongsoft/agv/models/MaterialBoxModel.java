package com.furongsoft.agv.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 料框信息
 *
 * @author linyehai
 */
@Getter
@Setter
public class MaterialBoxModel implements Serializable {
    private long id;

    /**
     * 料框二维码
     */
    private String qrCode;

    /**
     * 容器编码
     */
    private String code;

    /**
     * 料框名称
     */
    private String name;

    /**
     * 状态[0：空车；1：有货]
     */
    private int state;

    /**
     * 是否启用
     */
    private Integer enabled;

    /**
     * 原料集合
     */
    private List<MaterialBoxMaterialModel> materialBoxMaterialModels;
}
