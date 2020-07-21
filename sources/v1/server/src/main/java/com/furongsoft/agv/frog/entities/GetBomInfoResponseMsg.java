package com.furongsoft.agv.frog.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 获取Bom信息响应消息
 *
 * @author linyehai
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetBomInfoResponseMsg extends BaseResponseMsg {

    /**
     * erp.basedoc.bominfo.get请求的返回对象
     */
    private ResponseEntity erp_basedoc_bominfo_get_response;

    public GetBomInfoResponseMsg(String request_id, Boolean success, ErrorResponse error_response) {
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
         * 公司id
         */
        private String pk_corp;

        /**
         * bomid
         */
        private String pk_bomid;

        /**
         * 父项id
         */
        private String wlbmid;

        /**
         * 是否默认版本
         */
        private String sfmr;

        /**
         * 父项编码
         */
        private String wlbm;

        /**
         * 父项名称
         */
        private String wlmc;

        /**
         * 父项规格
         */
        private String fxinvspec;

        /**
         * 父项型号
         */
        private String fxinvtype;

        /**
         * BOM版本号
         */
        private String version;

        /**
         * 日期
         */
        private String sdate;

        /**
         * 单位
         */
        private String wlzjldwmc;

        /**
         * 父项数量
         */
        private Integer sl;

        /**
         * 单据唯一标识
         */
        private String pk_bom_bid;

        /**
         * 单据最后更新时间
         */
        private Date ts;

        /**
         * 单据是否作废
         */
        private Integer dr;

        /**
         * 子项存货id
         */
        private String zxbmid;

        /**
         * 子项存货编码
         */
        private String zxbm;

        /**
         * 子项存货名称
         */
        private String zxmc;

        /**
         * 规格
         */
        private String zxinvspec;

        /**
         * 型号
         */
        private String zxinvtype;

        /**
         * 子项数量
         */
        private Integer zxsl;

        /**
         * 子项单位
         */
        private String zxzjldwmc;
    }
}
