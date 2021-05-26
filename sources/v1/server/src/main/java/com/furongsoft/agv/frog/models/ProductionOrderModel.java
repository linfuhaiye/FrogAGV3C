package com.furongsoft.agv.frog.models;

import com.furongsoft.agv.frog.entities.GetBillFullInfoResponseMsg;
import com.furongsoft.agv.frog.entities.GetMoResponseMsg;
import com.furongsoft.base.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * BOM信息
 *
 * @author linyehai
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductionOrderModel extends BaseEntity {
    /**
     * bom信息
     */
    private BomModel bomModel;

    /**
     * 单据信息
     */
    private GetMoResponseMsg.DataEntity billInfo;

    /**
     * 领料单信息
     */
    private List<GetBillFullInfoResponseMsg.ItemEntity> billFullInfoItems;

    /**
     * 错误码。 0：成功。 -1：失败
     */
    private Integer errno;

    /**
     * 错误提示信息
     */
    private String errorMessage;

    public ProductionOrderModel(Integer errno, String errorMessage) {
        this.errno = errno;
        this.errorMessage = errorMessage;
    }
}
