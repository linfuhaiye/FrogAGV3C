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
 * 岗位管理
 *
 * @author chenfuqian
 */
@Entity
@Table(name = "t_sys_position")
@TableName("t_sys_position")
public class Position extends BaseEntity implements Serializable {

    /**
     * 索引
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 所属部门
     */
    private Long departmentId;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 岗位名称
     */
    @NotBlank(message = "position.name.notNull")
    @Length(max = 128, message = "position.name.maxLength")
    private String name;

    /**
     * 岗位类型
     */
    private Long type;

    /**
     * 岗位职责
     */
    private String duty;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
