package com.furongsoft.base.rbac.entities;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.furongsoft.base.entities.BaseEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 业务字典
 *
 * @author chenfuqian
 */
@Entity
@Table(name = "t_sys_dict")
@TableName("t_sys_dict")
public class Dictionary extends BaseEntity implements Serializable {

    /**
     * 索引
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 字典组别
     */
    private Long dictGroupId;

    /**
     * 字典组别编码
     */
    private String dictGroupCode;

    /**
     * 字典组别名称
     */
    @Transient
    @TableField(exist = false)
    private String groupName;

    /**
     * 字典描述
     */
    @NotBlank(message = "dict.name.notNull")
    @Length(max = 128, message = "dict.name.maxLength")
    private String name;

    /**
     * 字典编码
     */
    @NotBlank(message = "dict.code.notNull")
    @Length(max = 128, message = "dict.code.maxLength")
    private String code;

    /**
     * 排序
     */
    @Max(value = 127, message = "dict.sort.max")
    @Min(value = 0, message = "dict.sort.min")
    private Long sort;

    /**
     * 是否系统内置
     */
    private Boolean system;

    /**
     * 备注
     */
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDictGroupId() {
        return dictGroupId;
    }

    public void setDictGroupId(Long dictGroupId) {
        this.dictGroupId = dictGroupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public Boolean getSystem() {
        return system;
    }

    public void setSystem(Boolean system) {
        this.system = system;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDictGroupCode() {
        return dictGroupCode;
    }

    public void setDictGroupCode(String dictGroupCode) {
        this.dictGroupCode = dictGroupCode;
    }
}
