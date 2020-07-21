package com.furongsoft.agv.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 备货记录信息[站点历史备货数据]
 *
 * @author linyehai
 */
@Getter
@Setter
public class StockUpRecordModel implements Serializable {
    private long id;

    /**
     * 站点ID
     */
    private long siteId;

    /**
     * 料框ID
     */
    private long materialBoxId;

    /**
     * 状态[0：未出库；1：已出库]
     */
    private int state;

    /**
     * 类型[1：消毒间备货；2：拆包间备货；3：包材仓备货]
     */
    private int type;

    /**
     * 配送任务
     */
    private Long deliveryTaskId;

    /**
     * 是否启用
     */
    private Integer enabled;

    /**
     * 料车二维码
     */
    private String materialBoxQrCode;

    /**
     * 料车名称
     */
    private String materialBoxName;

    /**
     * 料车状态
     */
    private Integer materialBoxState;

    /**
     * 任务单号
     */
    private String deliveryTaskNo;

    /**
     * 料框信息
     */
    private MaterialBoxModel materialBoxModel;

    /**
     * 原料列表
     */
    private List<MaterialModel> materialModels;

    /**
     * 产品数量
     */
    private int count;

    /**
     * 产品ID
     */
    private Long materialId;

    /**
     * 原料类型
     */
    private int materialType;

    /**
     * 产品码
     */
    private String materialName;

    /**
     * 料车码
     */
    private String materialCarName;

    /**
     * 地标码
     */
    private String landMaskName;
}
