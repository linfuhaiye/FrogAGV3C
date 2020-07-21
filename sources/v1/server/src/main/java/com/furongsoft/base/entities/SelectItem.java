package com.furongsoft.base.entities;

/**
 * 选项
 *
 * @author chenfuqian
 */
public class SelectItem {
    /**
     * 名称
     */
    private String name;

    /**
     * 值
     */
    private String value;

    /**
     * 提示
     */
    private String hint;

    /**
     * 是否选中
     */
    private boolean selected;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean chose) {
        this.selected = chose;
    }
}
