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
 * 配货信息 TODO 未用上
 *
 * @author linyehai
 */
@Entity
@TableName("t_agv_distribution")
@Getter
@Setter
public class Distribution extends BaseEntity {
    @Id
    @GeneratedValue
    private long id;

    /**
     * 叫料ID
     */
    private long callMaterialId;

    /**
     * 配货数量
     */
    private int count;

    /**
     * 配货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date distributionTime;

    /**
     * 状态[0：未验收；1：部分验收；2：已验收]
     */
    private int state;

    /**
     * 类型[1：消毒间-灌装区；2：包材仓-包装区；3：拆包间-消毒间；4：包材仓-拆包间]  TODO 后续改用枚举
     */
    private int type;

    /**
     * 备货ID
     */
    private Integer stockUpId;

    /**
     * 是否启用
     */
    private Integer enabled;
}
