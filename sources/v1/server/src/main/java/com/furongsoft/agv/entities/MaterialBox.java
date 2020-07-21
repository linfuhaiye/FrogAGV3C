package com.furongsoft.agv.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.furongsoft.base.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

/**
 * 料框信息
 *
 * @author linyehai
 */
@Entity
@TableName("t_agv_material_box")
@Getter
@Setter
public class MaterialBox extends BaseEntity {
    @Id
    @GeneratedValue
    private long id;

    /**
     * 料框二维码
     */
    @Length(max = 255, message = "料框二维码不能超过255个字符")
    @NotBlank(message = "料框二维码不为空")
    private String qrCode;

    /**
     * 容器编码
     */
    private String code;

    /**
     * 料框名称
     */
    private String name;

    /**
     * 状态[0：空车；1：有货]  TODO 后续改用枚举
     */
    private int state;

    /**
     * 是否启用
     */
    private Integer enabled;
}
