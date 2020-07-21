package com.furongsoft.agv.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 叫料返回参数
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CallResponeModel {
    /**
     * 错误码： 0：无错误
     */
    private int errno;

    /**
     * 提示信息
     */
    private String message;
}
