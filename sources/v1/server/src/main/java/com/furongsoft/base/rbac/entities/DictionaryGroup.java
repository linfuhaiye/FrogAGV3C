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
 * 字典组
 *
 * @author chenfuqian
 */
@Entity
@Table(name = "t_sys_dict_group")
@TableName("t_sys_dict_group")
public class DictionaryGroup extends BaseEntity implements Serializable {

    /**
     * 索引
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 字典组名称
     */
    @NotBlank(message = "dictGroup.name.notNull")
    @Length(max = 128, message = "dictGroup.name.maxLength")
    private String name;

    /**
     * 字典组编码
     */
    @NotBlank(message = "dictGroup.code.notNull")
    @Length(max = 128, message = "dictGroup.code.maxLength")
    private String code;

    /**
     * 排序
     */
    @Max(value = 127, message = "dictGroup.sort.max")
    @Min(value = 0, message = "dictGroup.sort.min")
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
}
