package com.furongsoft.agv.frog.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 获取领料单信息请求消息
 *
 * @author linyehai
 */
@Getter
@Setter
public class GetBillFullInfoRequestMsg {
    /**
     * 需要返回的字段列表，多个字段用半角逗号分隔，可选值为返回示例中能看到的所有字段（表头）_true
     */
    private String fields;
    /**
     * 表体_true
     */
    private String fields_item;
    /**
     * 单据唯一标识ID_false
     */
    private String bill_pk;
    /**
     * 公司ID_true
     */
    private String pk_corp;
    /**
     * 领料单号_false
     */
    private String bill_code;
    /**
     * 生产订单号json数组_false
     */
    private List<CFirstBillCode> cfirstbillcode;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CFirstBillCode {
        /**
         * 生产订单号
         */
        private String mo_code;
    }
}
