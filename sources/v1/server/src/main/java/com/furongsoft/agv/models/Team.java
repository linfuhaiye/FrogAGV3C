package com.furongsoft.agv.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 班组
 *
 * @author linyehai
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team implements Serializable {
    /**
     * 主键
     */
    private long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 唯一标识
     */
    private String uuid;
}
