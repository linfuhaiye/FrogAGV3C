package com.furongsoft.agv.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.furongsoft.base.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 领料单详情信息 TODO 未用上
 *
 * @author linyehai
 */
@Entity
@TableName("t_agv_material_requisition_detail")
@Getter
@Setter
public class MaterialRequisitionDetail extends BaseEntity {
    @Id
    @GeneratedValue
    private long id;

    /**
     * 领料单ID
     */
    private long materialRequisitionId;

    /**
     * 物料ID(原料)
     */
    private long materialId;

    /**
     * 数量
     */
    private int count;

    /**
     * 是否启用
     */
    private Integer enabled;
}
