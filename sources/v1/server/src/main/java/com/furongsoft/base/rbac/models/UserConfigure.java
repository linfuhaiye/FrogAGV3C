package com.furongsoft.base.rbac.models;

import java.io.Serializable;

/**
 * 用户个人配置信息
 *
 * @author linyehai
 */
public class UserConfigure implements Serializable {

    /**
     * 配置索引
     */
    private long id;

    /**
     * 用户ID
     */
    private long userId;


    /**
     * 日报类型
     */
    private long reportType;

    /**
     * 日报类型名称
     */
    private String reportTypeName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getReportType() {
        return reportType;
    }

    public void setReportType(long reportType) {
        this.reportType = reportType;
    }

    public String getReportTypeName() {
        return reportTypeName;
    }

    public void setReportTypeName(String reportTypeName) {
        this.reportTypeName = reportTypeName;
    }
}
