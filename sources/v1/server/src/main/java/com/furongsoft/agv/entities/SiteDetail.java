package com.furongsoft.agv.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.furongsoft.base.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 站点详情信息[站点瞬时备货状态]
 *
 * @author linyehai
 */
@Entity
@TableName("t_agv_site_detail")
@Getter
@Setter
public class SiteDetail extends BaseEntity {
    @Id
    @GeneratedValue
    private long id;

    /**
     * 站点ID
     */
    private long siteId;

    /**
     * 料框ID
     */
    private Long materialBoxId;

    /**
     * 状态[0：空闲；1：锁定；2：有货]  TODO 后续改用枚举
     */
    private int state;

    /**
     * 配送任务
     */
    private Long deliveryTaskId;

    /**
     * 是否启用
     */
    private Integer enabled;
}
