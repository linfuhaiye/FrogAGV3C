package com.furongsoft.base.monitor.entities;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * 日志记录
 *
 * @author liujianning
 */
@TableName("t_sys_loginfo")
public class LogInfo implements Serializable {
    private static final long serialVersionUID = 1442602822345944955L;

    /**
     * 当前操作模块
     */
    @Excel(name = "当前操作模块", height = 20, width = 30)
    private String model;

    /**
     * 操作模块功能
     */
    @Excel(name = "操作模块功能", height = 20, width = 30)
    private String operation;

    /**
     * 执行时间
     */
    @Excel(name = "执行时间", height = 20, width = 30)
    private Float execTime;

    /**
     * 客户端ip
     */
    @Excel(name = "IP", height = 20, width = 30)
    private String ip;

    /**
     * 错误信息
     */
    @Excel(name = "错误信息", height = 20, width = 30)
    private String errLog;

    /**
     * 用户登录名
     */
    @Excel(name = "用户登录名", height = 20, width = 30)
    private String username;

    /**
     * 操作类型
     */
    @Excel(name = "操作类型", height = 20, width = 30)
    private String type;

    /**
     * 公司ID
     */
    @Excel(name = "公司ID", height = 20, width = 30)
    private String companyId;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "操作时间", height = 20, width = 30, format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建用户
     */
    @Excel(name = "用户ID", height = 20, width = 30)
    private Long createUser;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Float getExecTime() {
        return execTime;
    }

    public void setExecTime(Float execTime) {
        this.execTime = execTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getErrLog() {
        return errLog;
    }

    public void setErrLog(String errLog) {
        this.errLog = errLog;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }
}
