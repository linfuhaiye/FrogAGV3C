package com.furongsoft.agv.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.furongsoft.base.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 物料信息
 *
 * @author linyehai
 */
@Entity
@TableName("t_agv_material")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Material extends BaseEntity {
    @Id
    @GeneratedValue
    private long id;

    /**
     * 物料编号 TODO 加索引
     */
    private String code;

    /**
     * 物料名称
     */
    private String name;

    /**
     * 物料唯一标识
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
     * 是否启用
     */
    private Integer enabled;

    public Material(String code, String name, String uuid, String specs, String unit, String batch, Integer enabled) {
        this.code = code;
        this.name = name;
        this.uuid = uuid;
        this.specs = specs;
        this.unit = unit;
        this.batch = batch;
        this.enabled = enabled;
    }
}
