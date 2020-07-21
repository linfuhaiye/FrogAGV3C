package com.furongsoft.agv.frog.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 获取班组信息响应消息
 *
 * @author linyehai
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetBzResponseMsg extends BaseResponseMsg {

    /**
     * erp.mm.mo.get请求的返回对象
     */
    private ResponseEntity erp_basedoc_bz_get_response;

    public GetBzResponseMsg(String request_id, Boolean success, ErrorResponse error_response) {
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
         * 唯一标识
         */
        private String pk_defdoc;

        /**
         * 上级自定义项档案主键(树形结构)
         */
        private String pk_defdoc1;

        /**
         * 单据最后更新时间
         */
        private Date ts;

        /**
         * 编码
         */
        private String doccode;

        /**
         * 名称
         */
        private String docname;
    }
}
