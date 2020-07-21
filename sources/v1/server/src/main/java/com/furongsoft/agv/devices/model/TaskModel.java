package com.furongsoft.agv.devices.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 任务对象
 *
 * @author linyehai
 */
@Getter
@Setter
@AllArgsConstructor
public class TaskModel implements Serializable {
    /**
     * 按钮序号
     */
    private int buttonNo;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 任务时间
     */
    private Long timestamp;
}
