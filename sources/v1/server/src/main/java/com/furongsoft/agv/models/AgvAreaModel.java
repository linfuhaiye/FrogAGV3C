package com.furongsoft.agv.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 区域前端对象
 *
 * @author linyehai
 */
@Getter
@Setter
@ToString
public class AgvAreaModel implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 类型
     */
    private int type;

    /**
     * 子区域
     */
    private List<AgvAreaModel> children;
}
