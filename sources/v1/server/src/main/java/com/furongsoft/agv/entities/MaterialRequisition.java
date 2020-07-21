package com.furongsoft.agv.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.furongsoft.base.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 领料单信息 TODO 未用上
 *
 * @author linyehai
 */
@Entity
@TableName("t_agv_material_requisition")
@Getter
@Setter
public class MaterialRequisition extends BaseEntity {
    @Id
    @GeneratedValue
    private long id;

    /**
     * 领料单编号
     */
    private String code;

    /**
     * 领料时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date pickingTime;

    /**
     * 物料ID(产品)
     */
    private long materialId;

    /**
     * 是否启用
     */
    private Integer enabled;
}
