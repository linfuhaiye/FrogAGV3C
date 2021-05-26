package com.furongsoft.agv.devices.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.furongsoft.base.entities.BaseEntity;
import com.furongsoft.base.misc.StringUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 按钮设备配置表
 *
 * @author linyehai
 */
@Entity
@TableName("t_agv_call_button")
@Getter
@Setter
public class CallButton extends BaseEntity {
    @Id
    @GeneratedValue
    private long id;

    /**
     * 编号
     */
    private String code;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 设备唯一码
     */
    private String deviceKey;

    /**
     * 端口号
     */
    private int port;

    /**
     * 按钮编号
     */
    private String buttonCode;

    /**
     * 名称
     */
    private String name;

    /**
     * 站点ID
     */
    private Long siteId;

    /**
     * 是否启用
     */
    private Integer enabled;

    public boolean equals(String ipAddress, String code) {
        if (!StringUtils.isNullOrEmpty(ipAddress) && !StringUtils.isNullOrEmpty(code)) {
            return ipAddress.equals(this.ipAddress) && code.equals(this.code);
        } else {
            return false;
        }
    }
}
