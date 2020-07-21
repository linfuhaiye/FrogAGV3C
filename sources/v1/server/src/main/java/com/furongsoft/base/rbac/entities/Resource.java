package com.furongsoft.base.rbac.entities;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.furongsoft.base.entities.BaseEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 资源
 *
 * @author Alex
 */
@Entity
@Table(name = "t_sys_resource")
@TableName("t_sys_resource")
public class Resource extends BaseEntity implements Serializable {
    /**
     * 索引
     */
    @Id
    @GeneratedValue
    private Long id;

    private Long parentId;

    /**
     * 名称
     */
    @NotBlank(message = "resource.name.notNull")
    @Length(max = 50, message = "resource.name.maxLength")
    private String name;

    /**
     * 类型
     */
    @NotNull(message = "resource.type.notNull")
    private Integer type;

    /**
     * 是否系统内置:0 是, 1 否
     */
    private Boolean system;

    /**
     * 路径
     */
    @Length(max = 250, message = "resource.path.maxLength")
    private String path;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 图标路径
     */
    @Transient
    @TableField(exist = false)
    private String iconPath;

    /**
     * 权限编码
     */
    @Transient
    @TableField(exist = false)
    private String roleCode;

    /**
     * 图标
     */
    @JsonIgnore
    private String icon;

    /**
     * 备注
     */
    private String remark;

    /**
     * 优先级
     */
    @Min(value = 0, message = "resource.priority.min")
    @Max(value = 127, message = "resource.priority.max")
    private Integer priority;

    public Resource() {
    }

    public Resource(String name, Integer type, String path, Integer state, String remark) {
        this.name = name;
        this.type = type;
        this.path = path;
        this.state = state;
        this.remark = remark;
    }

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Boolean getSystem() {
        return system;
    }

    public void setSystem(Boolean system) {
        this.system = system;
    }
}
