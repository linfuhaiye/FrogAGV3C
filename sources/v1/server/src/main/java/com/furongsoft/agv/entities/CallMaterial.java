package com.furongsoft.agv.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.furongsoft.agv.models.WaveDetailModel;
import com.furongsoft.base.entities.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 叫料信息
 *
 * @author linyehai
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@TableName("t_agv_call_material")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CallMaterial extends BaseEntity {
    @Id
    @GeneratedValue
    private long id;

    /**
     * 原料物料ID
     */
    private long materialId;

    /**
     * 叫料数量
     */
    private int count;

    /**
     * 验收数量
     */
    private int acceptanceCount;

    /**
     * 状态[1：未配送；2：配送中；3：已完成；4：已取消]  TODO 后续改用枚举
     */
    private int state;

    /**
     * 叫料时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date callTime;

    /**
     * 配送波次详情编号
     */
    private String waveDetailCode;

    /**
     * 叫料区域类型[1：灌装区；2：包装区；3：消毒间；4：拆包间]
     */
    private int type;

    /**
     * 取消叫料原因
     */
    private String cancelReason;

    /**
     * 是否启用
     */
    private Integer enabled;

    /**
     * 班组唯一标识（青蛙uuid）
     */
    private String teamId;

    /**
     * 区域ID（产线ID）
     */
    private Long areaId;

    /**
     * 站点ID
     */
    private Long siteId;

    /**
     * 叫料人
     */
    private Long callMaterialUser;

    public CallMaterial(WaveDetailModel waveDetailModel) {
        this.materialId = waveDetailModel.getMaterialId();
        this.count = waveDetailModel.getCount();
        this.state = 1;
        this.waveDetailCode = waveDetailModel.getCode();
        this.type = waveDetailModel.getAreaType();
        this.enabled = 1;
    }
}
