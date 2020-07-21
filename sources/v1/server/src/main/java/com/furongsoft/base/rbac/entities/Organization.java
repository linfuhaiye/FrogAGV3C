package com.furongsoft.base.rbac.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.furongsoft.base.entities.BaseEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 组织架构
 *
 * @author chenfuqian
 */
@Entity
@Table(name = "t_sys_organization")
@TableName("t_sys_organization")
public class Organization extends BaseEntity implements Serializable {

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
     * 状态
     */
    private Integer state;

    /**
     * 部门名称
     */
    @NotBlank(message = "organization.name.notNull")
    @Length(max = 255, message = "organization.name.maxLength")
    private String name;

    /**
     * 部门编码
     */
    @NotBlank(message = "organization.code.notNull")
    @Length(max = 128, message = "organization.code.maxLength")
    private String code;
    /**
     * 排序号
     */
    @Min(value = 0, message = "organization.sort.min")
    @Max(value = 127, message = "organization.sort.max")
    private Integer sort;

    /**
     * 部门职责
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
