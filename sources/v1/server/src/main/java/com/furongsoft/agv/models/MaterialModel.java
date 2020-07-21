package com.furongsoft.agv.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 存货model类
 *
 * @author linyehai
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialModel implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 产品规格
     */
    private String specs;

    /**
     * 产品单位
     */
    private String unit;

    /**
     * 产品批次号
     */
    private String batch;

    /**
     * 数量
     */
    private int count;

    /**
     * 原料类型：[1：内包材；2：外包材；3：其它]
     */
    private int type;

    public MaterialModel(long id, String name, String uuid, int count, int type) {
        this.id = id;
        this.name = name;
        this.uuid = uuid;
        this.count = count;
        this.type = type;
    }
}
