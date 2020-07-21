package com.furongsoft.base.rbac.models;

import java.io.Serializable;
import java.util.List;

/**
 * 选择框分组对象,用于前端下拉选择框分组
 *
 * @author linyehai
 */
public class SelectGroup implements Serializable {

    /**
     * 标签
     */
    private String label;
    /**
     * 选项
     */
    private List<SelectModel> options;

    public SelectGroup() {
    }

    public SelectGroup(String label, List<SelectModel> options) {
        this.label = label;
        this.options = options;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<SelectModel> getOptions() {
        return options;
    }

    public void setOptions(List<SelectModel> options) {
        this.options = options;
    }
}
