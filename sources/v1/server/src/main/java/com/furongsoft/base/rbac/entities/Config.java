package com.furongsoft.base.rbac.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.furongsoft.base.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.io.Serializable;

/**
 * 系统配置表
 *
 * @author chenfuqian
 */
@Entity
@Table(name = "t_sys_config")
@TableName("t_sys_config")
@Getter
@Setter
public class Config extends BaseEntity implements Serializable {
    /**
     * 索引
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 附件ID
     */
    private Long attachmentId;

    /**
     * 附件服务器地址
     */
    private String attachmentServer;

    /**
     * 系统图标资源索引
     */
    private String logo;

    /**
     * 系统名称
     */
    private String name;

    /**
     * 级别
     */
    private String level;

    /**
     * 人脸识别率
     */
    @DecimalMin(value = "0.1", message = "人脸识别率不能小于0.1")
    @DecimalMax(value = "1", message = "人脸识别率不能大于1")
    private float threshold;

    private Integer level1;

    private Integer level2;

    private Integer level3;

}
