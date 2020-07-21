package com.furongsoft.agv.frog.entities;

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
 * BOM清单信息
 *
 * @author linyehai
 */
@Entity
@TableName("t_agv_bom_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BomDetail extends BaseEntity {
    @Id
    @GeneratedValue
    private long id;

    /**
     * BOM主键
     */
    private long bomId;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 比例数量
     */
    private int count;

    /**
     * 类型[1：内包材；2：外包材；3：其它]  TODO 后续改用枚举
     */
    private int type;

    /**
     * 是否启用
     */
    private Integer enabled;

    public BomDetail(long bomId, String materialCode, int count, int type, Integer enabled) {
        this.bomId = bomId;
        this.materialCode = materialCode;
        this.count = count;
        this.type = type;
        this.enabled = enabled;
    }
}
