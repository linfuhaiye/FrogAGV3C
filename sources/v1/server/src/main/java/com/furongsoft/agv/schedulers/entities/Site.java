package com.furongsoft.agv.schedulers.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 站点
 *
 * @author Alex
 */
@Getter
@Setter
@AllArgsConstructor
public class Site {
    /**
     * 编号
     */
    private String code;

    /**
     * 是否是空库位站点
     */
    private boolean isEmptySite;

    /**
     * 容器编码
     */
    private String containerId;
}
