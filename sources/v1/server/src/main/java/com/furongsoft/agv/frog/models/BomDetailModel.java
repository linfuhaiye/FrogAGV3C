package com.furongsoft.agv.frog.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * BOM清单信息
 *
 * @author linyehai
 */
@Getter
@Setter
public class BomDetailModel implements Serializable {
    private Long id;

    /**
     * BOM主键
     */
    private long bomId;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 一个产品里几个当前原料
     */
    private int count;

    /**
     * 类型[1：内包材；2：外包材；3：其它]
     */
    private int type;

    /**
     * 是否启用
     */
    private Integer enabled;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料ID
     */
    private Integer materialId;

    /**
     * 满料车数量
     */
    private Integer fullCount;
}
