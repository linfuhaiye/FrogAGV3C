package com.furongsoft.agv.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 配货任务信息前端对象
 *
 * @author linyehai
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DistributionTaskModel implements Serializable {
    /**
     * 波次编码
     */
    private String waveCode;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品UUID
     */
    private String productUuid;

    /**
     * 生产线编号
     */
    private String productLineCode;

    /**
     * 叫料时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date callTime;

    /**
     * 叫料集合
     */
    private List<CallMaterialModel> callMaterialModels;
}
