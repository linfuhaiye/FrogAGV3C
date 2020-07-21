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
 * BOM信息
 *
 * @author linyehai
 */
@Entity
@TableName("t_agv_bom")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bom extends BaseEntity {
    @Id
    @GeneratedValue
    private long id;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 满料车数量
     */
    private int fullCount;

    /**
     * 版本号
     */
    private String version;

    /**
     * 是否更新状态[0:未更新；1：已更新]  TODO 后续改用枚举
     */
    private int updateState;

    /**
     * 是否启用
     */
    private Integer enabled;

    public Bom(String materialCode, int fullCount, String version, Integer enabled, int updateState) {
        this.materialCode = materialCode;
        this.fullCount = fullCount;
        this.version = version;
        this.enabled = enabled;
        this.updateState = updateState;
    }
}
