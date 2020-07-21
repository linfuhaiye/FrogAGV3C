package com.furongsoft.agv.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.furongsoft.base.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 退货信息 TODO 未用上
 *
 * @author linyehai
 */
@Entity
@TableName("t_agv_sales_return")
@Getter
@Setter
public class SalesReturn extends BaseEntity {
    @Id
    @GeneratedValue
    private long id;

    /**
     * 叫料ID
     */
    private long callMaterialId;

    /**
     * 退货数量
     */
    private int count;

    /**
     * 退货原因
     */
    private String reason;

    /**
     * 退货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date returnTime;

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
     * 配送任务ID
     */
    private Long deliveryTaskId;

    /**
     * 是否启用
     */
    private Integer enabled;
}
