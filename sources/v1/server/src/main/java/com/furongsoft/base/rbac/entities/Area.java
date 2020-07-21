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
 * 区域信息
 *
 * @author chenfuqian
 */
@Entity
@Table(name = "t_sys_area")
@TableName("t_sys_area")
public class Area extends BaseEntity implements Serializable {

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
     * 区域代码
     */
    @NotBlank(message = "area.code.notNull")
    @Length(max = 32, message = "area.code.maxLength")
    private String code;

    /**
     * 排序号
     */
    @Length(max = 11, message = "area.priority.maxLength")
    private String priority;

    /**
     * 区域名称
     */
    @NotBlank(message = "area.name.notNull")
    @Length(max = 255, message = "area.name.maxLength")
    private String name;

    /**
     * 备注信息
     */
    @Length(max = 512, message = "area.remark.maxLength")
    private String remark;

    /**
     * 区域经理
     */
    private Long manager;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Long getManager() {
        return manager;
    }

    public void setManager(Long manager) {
        this.manager = manager;
    }
}
