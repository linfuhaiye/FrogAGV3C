package com.furongsoft.base.rbac.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.furongsoft.base.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 角色
 *
 * @author Alex
 */
@Entity
@Table(name = "t_sys_role")
@TableName("t_sys_role")
@Getter
@Setter
public class Role extends BaseEntity implements Serializable {
    /**
     * 索引
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "role.name.notNull")
    @Length(max = 20, message = "role.name.maxLength")
    private String name;

    /**
     * 角色编码
     */
    @Length(max = 64, message = "role.code.maxLength")
    private String code;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否系统内置
     */
    private Boolean system;


}
