package com.furongsoft.base.rbac.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.furongsoft.base.entities.BaseEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 权限
 *
 * @author Alex
 */
@Entity
@Table(name = "t_sys_permission")
@TableName("t_sys_permission")
public class Permission extends BaseEntity implements Serializable {
    /**
     * 索引
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "permission.name.notNull")
    @Length(max = 20, message = "permission.name.maxLength")
    private String name;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 备注
     */
    @Length(max = 255, message = "permission.remark.maxLength")
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
