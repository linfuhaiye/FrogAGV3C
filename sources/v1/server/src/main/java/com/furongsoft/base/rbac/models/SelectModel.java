package com.furongsoft.base.rbac.models;

import java.io.Serializable;

/**
 * 选择框模板
 *
 * @author linyehai
 */
public class SelectModel implements Serializable {

    /**
     * 值
     */
    private Object value;
    /**
     * 标签
     */
    private String label;

    public SelectModel() {
    }

    public SelectModel(Object value, String label) {
        this.value = value;
        this.label = label;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
