package com.furongsoft.agv.frog.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * 获取生产单请求消息
 *
 * @author linyehai
 */
@Getter
@Setter
public class GetMoRequestMsg {
    /**
     * 需要返回的字段列表，多个字段用半角逗号分隔，可选值为返回示例中能看到的所有字段_true
     */
    private String fields;

    /**
     * 查询三个月内创建时间开始。格式:yyyy-MM-dd HH:mm:ss_true
     */
    private String created_start;

    /**
     * 查询创建时间结束。格式:yyyy-MM-dd HH:mm:ss_true
     */
    private String created_end;

    /**
     * 状态（查看可选值），默认查询所有状态的数据，除了默认值外每次只能查询一种状态。_false
     */
    private String bill_status;

    /**
     * 单据所在公司ID，默认所有公司_false
     */
    private String bill_corpid;

    /**
     * 单据唯一标识ID_false
     */
    private String bill_pk;

    /**
     * 帐套 design_false
     */
    private String bill_code;

    /**
     * 计划开工日期_false
     */
    private String kgrq;

    /**
     * 页码。取值范围:大于零的整数; 默认值:1_false
     */
    private Integer page_no;

    /**
     * 每页条数。取值范围:大于零的整数; 默认值:40;最大值:100_false
     */
    private Integer page_size;

    /**
     * 是否启用has_next的分页方式，如果指定true,则返回的结果中不包含总记录数，但是会新增一个是否存在下一页的的字段，通过此种方式获取增量交易，接口调用成功率在原有的基础上有所提升。_false
     */
    private Boolean page_use_has_next;

    /**
     * 是否删除：0：在用；1：删除
     */
    private Integer dr;
}
