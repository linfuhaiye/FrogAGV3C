package com.furongsoft.agv.frog.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 获取生产单信息响应消息
 *
 * @author linyehai
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetMoResponseMsg extends BaseResponseMsg {

    /**
     * erp.mm.mo.get请求的返回对象
     */
    private ResponseEntity erp_mm_mo_get_response;

    public GetMoResponseMsg(String request_id, Boolean success, ErrorResponse error_response) {
        super(request_id, success, error_response);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseEntity {
        /**
         * 搜索到的交易信息总数
         */
        private Integer total_results;

        /**
         * 是否存在下一页
         */
        private Boolean has_next;

        /**
         * 搜索到的交易信息列表，返回的Trade和Order中包含的具体信息为入参fields请求的字段信息
         */
        private List<DataEntity> data;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataEntity {
        /**
         * 单据唯一标识
         */
        private String pk_bill;

        /**
         * 生产订单号
         */
        private String bill_code;

        /**
         * 单据所在公司PK
         */
        private String pk_corp;

        /**
         * 单据最后更新时间
         */
        private Date ts;

        /**
         * 单据是否作废
         */
        private Integer dr;

        /**
         * 来源单据号
         */
        private String lydjh;

        /**
         * 来源单据PK
         */
        private String lyid;

        /**
         * 来源单据行PK
         */
        private String lyfbid;

        /**
         * 生产订单号
         */
        private String scddh;

        /**
         * 部门PK
         */
        private String scbmid;

        /**
         * 部门Code
         */
        private String deptcode;

        /**
         * 部门Name
         */
        private String deptname;

        /**
         * 计划开工日期
         */
        private Date jhkgrq;

        /**
         * 计划完工日期
         */
        private Date jhwgrq;

        /**
         * 车间ID
         */
        private String pk_workshop;

        /**
         * 车间名称
         */
        private String workshopname;

        /**
         * 班组ID
         */
        private String pk_workgroup;

        /**
         * 班组名称
         */
        private String workgroupname;

        /**
         * 产线id
         */
        private String pk_productline;

        /**
         * 产线名称
         */
        private String productlinename;

        /**
         * 产线/设备号
         */
        private String pk_machine;

        /**
         * 生产序号
         */
        private String machinenumber;

        /**
         * 订单状态
         */
        private String zt;

        /**
         * 存货管理PK
         */
        private String pk_invmandoc;

        /**
         * 存货基本PK
         */
        private String pk_invbasdoc;

        /**
         * 存货生产管理PK
         */
        private String pk_produce;

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
         * 计划数量
         */
        private Integer jhsl;

        /**
         * 完工数量
         */
        private Integer wgsl;

        /**
         * 入库数量
         */
        private Integer rksl;

        /**
         * BOM版本号
         */
        private String bomver;

        /**
         * 制单人
         */
        private String zdrid;

        /**
         * 制单日期
         */
        private Date zdrq;
    }
}
