package com.furongsoft.agv.frog.entities;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 获获取领料单信息响应消息
 *
 * @author linyehai
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetBillFullInfoResponseMsg extends BaseResponseMsg {

    /**
     * erp_stock_bill_fullinfo_list请求的返回对象
     */
    private ResponseEntity erp_stock_bill_fullinfo_list_response;

    public GetBillFullInfoResponseMsg(String request_id, Boolean success, ErrorResponse error_response) {
        super(request_id, success, error_response);
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseEntity {
        /**
         * 搜索到的交易信息列表，返回的Trade和Order中包含的具体信息为入参fields请求的字段信息
         */
        private List<DataEntity> data;

    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataEntity {
        /**
         * 单据唯一标识
         */
        private String pk_bill;

        /**
         * 单据号
         */
        private String bill_code;

        /**
         * 单据所在公司PK
         */
        private String pk_corp;

        /**
         * 是否默认版本
         */
        private Date ts;

        /**
         * 单据是否作废
         */
        private Integer dr;

        /**
         * 单据日期
         */
        private Date dbilldate;

        /**
         * 收发类别类型 ID
         */
        private String cdispatcherid;

        /**
         * 收发类别编号
         */
        private String rdcode;

        /**
         * 收发类别名称
         */
        private String rdname;

        /**
         * 部门ID
         */
        private String cdptid;

        /**
         * 部门编号
         */
        private String deptcode;

        /**
         * 部门名称
         */
        private String deptname;

        /**
         * 仓库ID
         */
        private String cwarehouseid;

        /**
         * 仓库编号
         */
        private String storcode;

        /**
         * 仓库名称
         */
        private String storname;

        /**
         * 客商ID
         */
        private String ccustomerid;

        /**
         * 客商编号
         */
        private String custcode;

        /**
         * 客商名称
         */
        private String custname;

        /**
         * 审核日期
         */
        private Date dauditdate;

        /**
         * 退货标识
         */
        private Char freplenishflag;

        /**
         * 备注
         */
        private String vnote;

        /**
         * 表体明细
         */
        private List<ItemEntity> items;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemEntity {
        /**
         * 表体PK（唯一）
         */
        private String pk_bill_b;

        /**
         * 源头单据类型
         */
        private String cfirsttype;

        /**
         * 源头单据号(MO号)
         */
        private String cfirstbillcode;

        /**
         * 源头单据PK
         */
        private String cfirstbillhid;

        /**
         * 源头单据行ID
         */
        private String cfirstbillbid;

        /**
         * 来源单据号
         */
        private String csourcebillcode;

        /**
         * 来源单据PK
         */
        private String csourcebillhid;

        /**
         * 来源单据行id
         */
        private String csourcebillbid;

        /**
         * 来源单据类型
         */
        private String csourcetype;

        /**
         * 存货管理PK-领料物料
         */
        private String pk_invmandoc;

        /**
         * 存货基本PK-领料物料
         */
        private String pk_invbasdoc;

        /**
         * 存货编号
         */
        private String invcode;

        /**
         * 存货名称
         */
        private String invname;

        /**
         * 存货规格
         */
        private String invspec;

        /**
         * 单位
         */
        private String invunit;

        /**
         * 应出入数量
         */
        private BigDecimal nshouldnum;

        /**
         * 应出入辅数量
         */
        private BigDecimal nshouldassistnum;

        /**
         * 实出入数量
         */
        private BigDecimal nnum;

        /**
         * 实出入辅数量
         */
        private BigDecimal nassistnum;

        /**
         * 批次号
         */
        private String vbatchcode;

        /**
         * 是否默认版本
         */
        private Date ts;

        /**
         * 单据是否作废
         */
        private Integer dr;

    }
}
