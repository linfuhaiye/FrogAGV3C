package com.furongsoft.agv.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.furongsoft.base.entities.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Agv系统配置表
 *
 * @author linyehai
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@TableName("t_agv_config")
@Getter
@Setter
public class AgvConfig extends BaseEntity {
    /**
     * 索引
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 站点二维码关键字
     */
    private String siteCode;

    /**
     * 料框二维码关键字
     */
    private String materialCarCode;

    /**
     * 是否启用
     */
    private int enabled;
}
