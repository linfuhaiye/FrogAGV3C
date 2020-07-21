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
 * 料框-物料信息
 *
 * @author linyehai
 */
@Entity
@TableName("t_agv_material_box_material")
@Getter
@Setter
@NoArgsConstructor
public class MaterialBoxMaterial extends BaseEntity {
    @Id
    @GeneratedValue
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

    public MaterialBoxMaterial(long materialBoxId, long materialId, int count, int state) {
        this.materialBoxId = materialBoxId;
        this.materialId = materialId;
        this.count = count;
        this.state = state;
        this.enabled = 1;
    }
}
