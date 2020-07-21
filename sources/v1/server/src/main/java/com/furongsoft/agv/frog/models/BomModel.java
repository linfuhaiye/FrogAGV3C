package com.furongsoft.agv.frog.models;

import com.furongsoft.agv.frog.entities.BomDetail;
import com.furongsoft.base.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * BOM信息
 *
 * @author linyehai
 */
@Getter
@Setter
public class BomModel extends BaseEntity {
    private Long id;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 满料车数量
     */
    private int fullCount;

    /**
     * 版本号
     */
    private String version;

    /**
     * 是否启用
     */
    private Integer enabled;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 更新状态
     */
    private int updateState;

    /**
     * BOM详情列表
     */
    private List<BomDetail> bomDetails;
}
