package com.furongsoft.agv.schedulers.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 物料
 *
 * @author Alex
 */
@Getter
@Setter
@AllArgsConstructor
public class Material {
    /**
     * 物料编码
     */
    private String code;

    /**
     * 数量
     */
    private Integer count;
}
