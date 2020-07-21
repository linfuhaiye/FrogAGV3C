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
 * 验收信息 TODO 未用上
 *
 * @author linyehai
 */
@Entity
@TableName("t_agv_acceptance")
@Getter
@Setter
public class Acceptance extends BaseEntity {
    @Id
    @GeneratedValue
    private long id;

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
     * 验收人 TODO
     */
    private Long acceptanceUser;

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
