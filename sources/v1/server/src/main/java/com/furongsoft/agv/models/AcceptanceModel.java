package com.furongsoft.agv.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 验收信息前端对象
 *
 * @author linyehai
 */
@Getter
@Setter
public class AcceptanceModel implements Serializable {

    /**
     * 叫料ID
     */
    private long callMaterialId;

    /**
     * 验收数量
     */
    private int count;

    /**
     * 验收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date acceptanceTime;

    /**
     * 班组唯一标识（uuid）
     */
    private String teamId;

    /**
     * 班组名称
     */
    private String teamName;

    /**
     * 区域ID(产线ID)
     */
    private Long areaId;

    /**
     * 产线ID
     */
    private Long productId;

    /**
     * 配送任务ID
     */
    private Long deliveryTaskId;

    /**
     * 是否启用
     */
    private Integer enabled;

}
