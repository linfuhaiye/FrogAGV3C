package com.furongsoft.base.rbac.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.furongsoft.base.entities.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 用户个人配置
 *
 * @author linyehai
 */
@Entity
@Table(name = "t_sys_user_configure")
@TableName("t_sys_user_configure")
public class UserConfigure extends BaseEntity implements Serializable {
    /**
     * 索引
     */
    @Id
    @GeneratedValue
    private long id;

    /**
     * 用户索引
     */
    private long userId;

    /**
     * 日报类型
     */
    private long reportType;

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
}
