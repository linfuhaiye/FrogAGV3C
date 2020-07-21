package com.furongsoft.agv.entities;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.furongsoft.base.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * 站点信息
 *
 * @author linyehai
 */
@Entity
@TableName("t_agv_site")
@Getter
@Setter
public class Site extends BaseEntity {
    @Id
    @GeneratedValue
    private long id;

    /**
     * 二维码
     */
    private String qrCode;

    /**
     * X轴坐标
     */
    private Double locationX;

    /**
     * Y轴坐标
     */
    private Double locationY;

    /**
     * Z轴坐标
     */
    private Double locationZ;

    /**
     * 类型[1：备货位；2：出货位；3：空车位；]  TODO 后续改用枚举
     */
    private int type;

    /**
     * 名称
     */
    private String name;

    /**
     * 编号
     */
    private String code;

    /**
     * 是否启用
     */
    private Integer enabled;

    /**
     * 任务单号
     */
    @Transient
    @TableField(exist = false)
    private String orderNo;
}
