package com.furongsoft.base.order.entities;

import java.util.Date;

/**
 * 订单号实体类
 *
 * @author chenfuqian
 */
public class OrderNo {

    /**
     * 主键
     */
    private long id;

    /**
     * 业务编号
     */
    private String code;

    /**
     * 日期
     */
    private Date orderDate;

    /**
     * 编号
     */
    private long orderNo;

    /**
     * 今天有没有编号
     */
    private Boolean flag;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(long orderNo) {
        this.orderNo = orderNo;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}
